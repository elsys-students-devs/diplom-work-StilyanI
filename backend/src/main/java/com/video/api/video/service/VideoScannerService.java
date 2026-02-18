package com.video.api.video.service;

import com.video.api.video.config.VideoProperties;
import com.video.api.video.model.FolderSearchResult;
import com.video.api.video.model.MovieFolder;
import com.video.api.video.model.ShowEpisodeFile;
import com.video.api.video.model.ShowFolder;
import com.video.api.video.model.ShowSeasonFolder;
import com.video.api.video.model.Video;
import com.video.api.video.repository.VideoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class VideoScannerService {
    private static final Set<String> VIDEO_EXTENSIONS = Set.of(
            ".mp4", ".mkv", ".avi", ".mov", ".m4v", ".webm"
    );
    private static final Pattern TITLE_FORMAT_PATTERN = Pattern.compile("^(.+?)(?:\\s*\\((\\d{4})\\))?(?:\\s*\\[(.+?)])?$");
    private static final Pattern SEASON_PATTERN = Pattern.compile("(?i)(?:\\bS(\\d{1,2})\\b|\\bSeason\\s*(\\d{1,2})\\b)");
    private static final Pattern EPISODE_PATTERN = Pattern.compile("(?i)\\bE(\\d{1,3})\\b");

    private final VideoProperties videoProperties;
    private final VideoRepository videoRepository;

    public VideoScannerService(VideoProperties videoProperties, VideoRepository videoRepository) {
        this.videoProperties = videoProperties;
        this.videoRepository = videoRepository;
    }

    public FolderSearchResult getFoldersList() {
        Path storageRoot = Paths.get(videoProperties.getStoragePath());
        Path moviesRoot = storageRoot.resolve("Movies");
        Path showsRoot = storageRoot.resolve("Shows");

        FolderSearchResult result = new FolderSearchResult();
        result.setMovies(new ArrayList<>());
        result.setShows(new ArrayList<>());

        scanMovies(moviesRoot, result.getMovies());
        scanShows(showsRoot, result.getShows());

        return result;
    }

    private void scanMovies(Path moviesRoot, List<MovieFolder> out) {
        if (!Files.isDirectory(moviesRoot)) return;

        try (Stream<Path> stream = Files.list(moviesRoot)) {
            List<Path> movieDirs = stream
                    .filter(Files::isDirectory)
                    .sorted(Comparator.comparing(p -> p.getFileName().toString().toLowerCase()))
                    .toList();

            for (Path movieDir : movieDirs) {
                ParsedMeta meta = parseMeta(movieDir.getFileName().toString());
                Optional<Path> videoFile = firstVideoFile(movieDir);
                if (videoFile.isEmpty()) continue;

                Long id = getOrCreateVideoId(videoFile.get());

                out.add(new MovieFolder(
                        id,
                        meta.title,
                        meta.year,
                        meta.imdbId
                ));
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to scan Movies folder: " + moviesRoot, e);
        }
    }

    private void scanShows(Path showsRoot, List<ShowFolder> out) {
        if (!Files.isDirectory(showsRoot)) return;

        try (Stream<Path> stream = Files.list(showsRoot)) {
            List<Path> showDirs = stream
                    .filter(Files::isDirectory)
                    .sorted(Comparator.comparing(p -> p.getFileName().toString().toLowerCase()))
                    .toList();

            for (Path showDir : showDirs) {
                ParsedMeta meta = parseMeta(showDir.getFileName().toString());
                List<ShowSeasonFolder> seasons = scanSeasons(showDir);

                if (!seasons.isEmpty()) {
                    out.add(new ShowFolder(
                            meta.title,
                            meta.year,
                            meta.imdbId,
                            seasons
                    ));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to scan Shows folder: " + showsRoot, e);
        }
    }

    private List<ShowSeasonFolder> scanSeasons(Path showDir) throws IOException {
        List<ShowSeasonFolder> seasons = new ArrayList<>();

        try (Stream<Path> stream = Files.list(showDir)) {
            List<Path> seasonDirs = stream
                    .filter(Files::isDirectory)
                    .sorted(Comparator.comparingInt(p -> parseSeasonNumber(p.getFileName().toString()).orElse(Integer.MAX_VALUE)))
                    .toList();

            for (Path seasonDir : seasonDirs) {
                Optional<Integer> seasonNumOpt = parseSeasonNumber(seasonDir.getFileName().toString());
                if (seasonNumOpt.isEmpty()) continue;

                int seasonNumber = seasonNumOpt.get();
                List<ShowEpisodeFile> episodes = scanEpisodes(seasonDir);

                if (!episodes.isEmpty()) {
                    seasons.add(new ShowSeasonFolder(seasonNumber, episodes));
                }
            }
        }

        return seasons;
    }

    private List<ShowEpisodeFile> scanEpisodes(Path seasonDir) throws IOException {
        List<ShowEpisodeFile> episodes = new ArrayList<>();

        try (Stream<Path> stream = Files.list(seasonDir)) {
            List<Path> episodeFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(this::isVideoFile)
                    .sorted(Comparator
                            .comparingInt((Path p) -> parseEpisodeNumber(stripExtension(p.getFileName().toString())).orElse(Integer.MAX_VALUE))
                            .thenComparing(p -> p.getFileName().toString().toLowerCase()))
                    .toList();

            int fallbackEpisode = 1;
            for (Path epFile : episodeFiles) {
                Integer episodeNumber = parseEpisodeNumber(stripExtension(epFile.getFileName().toString()))
                        .orElse(fallbackEpisode++);
                Long id = getOrCreateVideoId(epFile);

                episodes.add(new ShowEpisodeFile(episodeNumber, id));
            }
        }

        return episodes;
    }

    private Optional<Path> firstVideoFile(Path dir) {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(this::isVideoFile).min(Comparator.comparing(p -> p.getFileName().toString().toLowerCase()));
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read directory: " + dir, e);
        }
    }

    private boolean isVideoFile(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        for (String ext : VIDEO_EXTENSIONS) {
            if (name.endsWith(ext)) return true;
        }
        return false;
    }

    private Long getOrCreateVideoId(Path filePath) {
        String normalized = filePath.toAbsolutePath().normalize().toString();

        return videoRepository.findVideoByPathToVideo(normalized)
                .map(Video::getId)
                .orElseGet(() -> videoRepository.save(new Video(null, normalized)).getId());
    }

    private ParsedMeta parseMeta(String rawFolderName) {
        String raw = rawFolderName == null ? "" : rawFolderName.trim();
        if (raw.isBlank()) {
            return new ParsedMeta(null, null, null);
        }

        Matcher matcher = TITLE_FORMAT_PATTERN.matcher(raw);
        if (!matcher.matches()) {
            return new ParsedMeta(raw, null, null);
        }

        String title = matcher.group(1) != null ? matcher.group(1).trim() : null;

        Integer year = null;
        String yearGroup = matcher.group(2);
        if (yearGroup != null && !yearGroup.isBlank()) {
            year = Integer.parseInt(yearGroup);
        }

        String imdbId = matcher.group(3);
        if (imdbId != null) {
            imdbId = imdbId.trim();
            if (imdbId.isBlank()) imdbId = null;
        }

        return new ParsedMeta(title, year, imdbId);
    }

    private Optional<Integer> parseSeasonNumber(String seasonFolderName) {
        Matcher m = SEASON_PATTERN.matcher(seasonFolderName);
        if (!m.find()) return Optional.empty();

        String g1 = m.group(1);
        String g2 = m.group(2);
        String val = (g1 != null) ? g1 : g2;

        try {
            return Optional.of(Integer.parseInt(val));
        } catch (NumberFormatException _) {
            return Optional.empty();
        }
    }

    private Optional<Integer> parseEpisodeNumber(String fileBaseName) {
        Matcher m = EPISODE_PATTERN.matcher(fileBaseName);
        if (m.find()) {
            return Optional.of(Integer.parseInt(m.group(1)));
        }

        return Optional.empty();
    }

    private String stripExtension(String fileName) {
        int idx = fileName.lastIndexOf('.');
        return (idx > 0) ? fileName.substring(0, idx) : fileName;
    }

    @AllArgsConstructor
    private static class ParsedMeta {
        String title;
        Integer year;
        String imdbId;
    }
}
