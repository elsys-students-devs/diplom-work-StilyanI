package com.video.api.video.service;

import com.video.api.video.config.VideoProperties;
import com.video.api.video.exception.InvalidStreamQualityException;
import com.video.api.video.exception.PlaylistFailedCreationException;
import com.video.api.video.exception.TranscodingFailedException;
import com.video.api.video.exception.VideoFileNotFoundException;
import com.video.api.video.model.StreamQuality;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.video.api.video.util.FileBrowsingUtils.parseDirectories;
import static com.video.api.video.util.VideoUtils.getVideoDuration;
import static com.video.api.video.util.VideoUtils.scanHighestSegment;
import static com.video.api.video.util.VideoUtils.segmentPath;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {
    private static final int POLL_INTERVAL_MS = 200;

    private static final int SEGMENT_TIMEOUT_MS = 30_000;

    private final TranscodingService transcodingService;
    private final VideoProperties videoProperties;

    public VideoServiceImpl(TranscodingService transcodingService, VideoProperties videoProperties) {
        this.transcodingService = transcodingService;
        this.videoProperties = videoProperties;
    }

    @Override
    public ResponseEntity<Resource> getVideoMasterPlaylist(String videoId) {
        log.info("Getting video master playlist for video id {}", videoId);
        Path sourcePath = resolveSourceVideo(videoId);

        if (sourcePath == null) {
            throw new VideoFileNotFoundException("Source video not found for videoId=" + videoId);
        }

        Path masterPath = Paths.get(videoProperties.getHlsOutputPath(), videoId, "master.m3u8");

        if(!Files.exists(masterPath)) {
            String masterContent = StreamQuality.QUALITY_LIST.stream()
                    .map(p -> {
                        int videoBandwidthKbps = Integer.parseInt(p.videoBitrate().replace("k", ""));
                        int audioBandwidthKbps = Integer.parseInt(p.audioBitrate().replace("k", ""));
                        int totalBandwidth = (videoBandwidthKbps + audioBandwidthKbps) * 1000;

                        return "#EXT-X-STREAM-INF:BANDWIDTH=" + totalBandwidth
                                + ",RESOLUTION=" + p.width() + "x" + p.height() + "\n"
                                + p.name() + "/playlist.m3u8";
                    })
                    .collect(Collectors.joining("\n", "#EXTM3U\n", "\n"));

            try {
                Files.createDirectories(masterPath.getParent());
                Files.writeString(masterPath, masterContent);
            } catch (Exception e) {
                throw new PlaylistFailedCreationException("Failed to create master playlist for videoId=" + videoId);
            }
        }

        return ResponseEntity.ok()
                .contentType(new MediaType("application", "x-mpegURL"))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .body(new FileSystemResource(masterPath));
    }

    @Override
    public ResponseEntity<Resource> getVideoPlaylist(String videoId, String quality) {
        Path sourcePath = resolveSourceVideo(videoId);
        StreamQuality streamQuality = StreamQuality.byName(quality);

        if (sourcePath == null) {
            throw new VideoFileNotFoundException("Source video not found for videoId=" + videoId);
        }
        if(streamQuality == null) {
            throw new InvalidStreamQualityException("Given stream quality(" + quality + ") is not valid. Available stream qualities are: " + StreamQuality.QUALITY_LIST.stream().map(StreamQuality::name).toList());
        }

        Path outputDir = Paths.get(videoProperties.getHlsOutputPath(), videoId, quality);
        Path playlistPath = outputDir.resolve("playlist-temp.m3u8");

        if(!Files.exists(playlistPath)) {
            transcodingService.ensureTranscoding(sourcePath, outputDir, streamQuality, 0);

            long deadline = System.currentTimeMillis() + SEGMENT_TIMEOUT_MS;
            while (scanHighestSegment(outputDir) < 0) {

                if (transcodingService.hasJobFailed(sourcePath, quality)) {
                    throw new TranscodingFailedException("Transcoding failed for videoId=" + videoId);
                }
                if (System.currentTimeMillis() > deadline) {
                    throw new TranscodingFailedException("Transcoding timed out for videoId=" + videoId);
                }
                sleep();
            }

            try {
                String playlistContent = buildPlaylist(sourcePath);
                Files.writeString(playlistPath, playlistContent);
            } catch (Exception e) {
                throw new PlaylistFailedCreationException("Failed to create variant playlist for videoId=" + videoId + ", quality=" + quality);
            }
        }

        return ResponseEntity.ok()
                .contentType(new MediaType("application", "x-mpegURL"))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .body(new FileSystemResource(playlistPath));
    }

    @Override
    public ResponseEntity<Resource> getVideoSegment(String videoId, String quality, Integer segmentNumber) {
        Path sourcePath = resolveSourceVideo(videoId);
        StreamQuality streamQuality = StreamQuality.byName(quality);

        if (sourcePath == null) {
            throw new VideoFileNotFoundException("Source video not found for videoId=" + videoId);
        }
        if(streamQuality == null) {
            throw new InvalidStreamQualityException("Given stream quality(" + quality + ") is not valid. Available stream qualities are: " + StreamQuality.QUALITY_LIST.stream().map(StreamQuality::name).toList());
        }

        Path outputDir = Paths.get(videoProperties.getHlsOutputPath(), videoId, quality);
        Path segmentPath = segmentPath(outputDir, segmentNumber);

        if(!Files.exists(segmentPath)) {
            transcodingService.ensureTranscoding(sourcePath, outputDir, streamQuality, segmentNumber);

            long deadline = System.currentTimeMillis() + SEGMENT_TIMEOUT_MS;
            while (!transcodingService.isSegmentReady(segmentPath)) {
                if (transcodingService.hasJobFailed(sourcePath, quality)) {
                    throw new TranscodingFailedException("Transcoding failed for videoId=" + videoId);
                }
                if (System.currentTimeMillis() > deadline) {
                    throw new TranscodingFailedException("Transcoding timed out for videoId=" + videoId);
                }
                sleep();
            }
        }

        return ResponseEntity.ok()
                .contentType(new MediaType("video", "mp2t"))
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET")
                .body(new FileSystemResource(segmentPath));
    }

    @Override
    public Map<String, List<Map<String, String>>> getFoldersList() {
        List<Map<String, String>> parsedMovieDirectories = parseDirectories(videoProperties.getStoragePath() + "/Movies");
        List<Map<String, String>> parsedShowDirectories = parseDirectories(videoProperties.getStoragePath() + "/Shows");

        Map<String, List<Map<String, String>>> result = new HashMap<>();
        result.put("Movies", parsedMovieDirectories);
        result.put("Shows", parsedShowDirectories);

        return result;
    }

    private Path resolveSourceVideo(String videoId) {
        Path root = Paths.get(videoProperties.getStoragePath());
        try (Stream<Path> walk = Files.walk(root)) {
            return walk
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString();
                        String nameWithoutExt = name.contains(".")
                                ? name.substring(0, name.lastIndexOf('.'))
                                : name;
                        return nameWithoutExt.equals(videoId);
                    })
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error resolving videoId={}", videoId, e);
            return null;
        }
    }

    private String buildPlaylist(Path sourcePath){

        double duration = getVideoDuration(sourcePath);
        int numOfSegments = (int) Math.ceil(duration / videoProperties.getSegmentDuration());

        StringBuilder sb = new StringBuilder();
        sb.append("#EXTM3U\n");
        sb.append("#EXT-X-VERSION:3\n");
        sb.append("#EXT-X-TARGETDURATION:").append(videoProperties.getSegmentDuration()).append("\n");
        sb.append("#EXT-X-MEDIA-SEQUENCE:0\n");
        sb.append("#EXT-X-PLAYLIST-TYPE:VOD\n");

        for (int i = 0; i < numOfSegments; i++) {
            sb.append("#EXTINF:");
            if(i == numOfSegments - 1) {
                sb.append(duration - i*10).append(",\n");
            } else {
                sb.append(videoProperties.getSegmentDuration()).append(".0,\n");
            }
            sb.append(String.format("segment%03d.ts\n", i));
        }

        sb.append("#EXT-X-ENDLIST\n");
        return sb.toString();
    }

    private static void sleep() {
        try {
            Thread.sleep((long) VideoServiceImpl.POLL_INTERVAL_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
