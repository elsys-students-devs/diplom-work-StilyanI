package com.video.api.metadata.service;

import com.video.api.metadata.exception.ResponseReadingFailureException;
import com.video.api.metadata.exception.TMDBResponseException;
import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TMDBImagesResponse;
import com.video.api.metadata.model.TMDBLogo;
import com.video.api.metadata.model.TMDBSearchIdResponse;
import com.video.api.metadata.model.TMDBSearchResponse;
import com.video.api.metadata.model.TvEpisode;
import com.video.api.video.model.FolderSearchResult;
import com.video.api.video.model.MovieFolder;
import com.video.api.video.model.ShowFolder;
import com.video.api.video.service.VideoScannerService;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class MetadataServiceImpl implements MetadataService {

    private final OkHttpClient client;
    private final JsonMapper jsonMapper;
    private final VideoScannerService  videoScannerService;

    MetadataServiceImpl metadataService;

    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String LANGUAGE = "language";
    private static final String EN_US = "en-US";
    private static final String TMDB_IMAGE_URL = "https://image.tmdb.org/t/p/original";

    public MetadataServiceImpl(OkHttpClient client, JsonMapper jsonMapper, VideoScannerService videoScannerService) {
        this.client = client;
        this.jsonMapper = jsonMapper;
        this.videoScannerService = videoScannerService;
    }

    private <T> T execute(Request request, Class<T> responseType) {
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();

            if (!response.isSuccessful()) {
                throw new TMDBResponseException(body, response);
            }

            return jsonMapper.readValue(body, responseType);

        } catch (IOException e) {
            throw new ResponseReadingFailureException(e.getMessage());
        }
    }

    private Request buildRequest(HttpUrl url){
        return new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .build();
    }

    @Override
    public Media[] getAll() {
        FolderSearchResult folders = videoScannerService.getFoldersList();
        if (folders == null) return new Media[0];

        List<Media> result = new ArrayList<>();
        Set<Integer> seenTmdbIds = new HashSet<>();

        if (folders.getMovies() != null) {
            for (MovieFolder movie : folders.getMovies()) {
                Media media = resolveMedia(
                        movie.getTitle(),
                        movie.getYear(),
                        movie.getImdbId(),
                        MediaType.MOVIE
                );

                media = enrichMedia(media, movie.getId(), MediaType.MOVIE);

                if (media != null && seenTmdbIds.add(media.getTmdbId())) {
                    result.add(media);
                }
            }
        }

        if (folders.getShows() != null) {
            for (ShowFolder show : folders.getShows()) {
                Media media = resolveMedia(
                        show.getTitle(),
                        show.getYear(),
                        show.getImdbId(),
                        MediaType.TVSHOW
                );

                media = enrichMedia(media, null, MediaType.TVSHOW);

                if (media != null && seenTmdbIds.add(media.getTmdbId())) {
                    result.add(media);
                }
            }
        }

        return result.toArray(Media[]::new);
    }

    private Media resolveMedia(String title, Integer year, String imdbId, MediaType type) {
        if (imdbId != null && !imdbId.isBlank()) {
            try {
                Media byImdb = metadataService.findById(imdbId.trim());
                if (byImdb != null) return byImdb;
            } catch (Exception _) {
                //Continue with other methods
            }
        }

        if (title == null || title.isBlank()) return null;

        try {
            return metadataService.search(title.trim(), type, year);
        } catch (Exception _) {
            try {
                return search(title.trim(), type);
            } catch (Exception _) {
                return null;
            }
        }
    }

    private Media enrichMedia(Media base, Long localVideoFileId, MediaType mediaType) {
        if (base == null) return null;

        String logoPath = getLogoPath(base.getTmdbId(), mediaType);

        base.setId(localVideoFileId);
        base.setLogoPath(TMDB_IMAGE_URL + logoPath);
        base.setBackdropPath(TMDB_IMAGE_URL + base.getBackdropPath());
        base.setPosterPath(TMDB_IMAGE_URL + base.getPosterPath());
        base.setType(mediaType.getType());

        return base;
    }

    private String getLogoPath(int tmdbId, MediaType mediaType) {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder()
                .addPathSegment(mediaType.getType())
                .addPathSegment(String.valueOf(tmdbId))
                .addPathSegment("images")
                .addQueryParameter(LANGUAGE, EN_US)
                .build();

        Request request = buildRequest(url);
        TMDBImagesResponse images = execute(request, TMDBImagesResponse.class);

        if (images == null || images.logos() == null || images.logos().isEmpty()) {
            return null;
        }

        return images.logos().stream()
                .sorted(Comparator
                        .comparing((TMDBLogo logo) -> !"en".equalsIgnoreCase(logo.iso6391()))
                        .thenComparing(Comparator.comparingDouble(TMDBLogo::voteAverage).reversed()))
                .map(TMDBLogo::filePath)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }


    @Override
    public Media search(String name, MediaType mediaType, HashMap<String, String> otherParameters) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder()
                .addPathSegment("search")
                .addPathSegment(mediaType.getType())
                .addQueryParameter("query", name)
                .addQueryParameter("include_adult", "false")
                .addQueryParameter(LANGUAGE, EN_US)
                .addQueryParameter("page", "1");
        if (otherParameters != null) {
            otherParameters.forEach(urlBuilder::addQueryParameter);
        }
        HttpUrl url = urlBuilder.build();

        Request request = buildRequest(url);

        TMDBSearchResponse searchResponse = execute(request, TMDBSearchResponse.class);
        return searchResponse.results().getFirst();
    }

    @Override
    public Media search(String name, MediaType mediaType) {
        return search(name, mediaType, (HashMap<String, String>) null);
    }

    @Cacheable(
            cacheNames = "tmdb-cache",
            key = "#name + '-' + #mediaType + '-' + #year"
    )
    @Override
    public Media search(String name, MediaType mediaType, Integer year) {
        if(year == null)
            return search(name, mediaType);
        else {
            HashMap<String, String> params = new HashMap<>();
            params.put("year", year.toString());

            return search(name, mediaType, params);
        }
    }

    @Cacheable(
            cacheNames = "tmdb-cache",
            key = "#id"
    )
    @Override
    public Media findById(String id) {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder()
                .addPathSegment("find")
                .addPathSegment(id)
                .addQueryParameter("external_source", "imdb_id")
                .addQueryParameter(LANGUAGE, EN_US)
                .build();

        Request request = buildRequest(url);

        TMDBSearchIdResponse searchIdResponse = execute(request, TMDBSearchIdResponse.class);
        if(!searchIdResponse.movieResults().isEmpty()) return searchIdResponse.movieResults().getFirst();
            else if(!searchIdResponse.tvResults().isEmpty()) return searchIdResponse.tvResults().getFirst();
            else return null;
    }

    @Cacheable(
            cacheNames = "tmdb-cache",
            key = "#seriesId + '-' + #seasonNumber + '-' + #episodeNumber"
    )
    @Override
    public TvEpisode getTvEpisode(int seriesId, int seasonNumber, int episodeNumber) {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder()
                .addPathSegment("tv")
                .addPathSegment(String.valueOf(seriesId))
                .addPathSegment("season")
                .addPathSegment(String.valueOf(seasonNumber))
                .addPathSegment("episode")
                .addPathSegment(String.valueOf(episodeNumber))
                .addQueryParameter(LANGUAGE, EN_US)
                .build();

        Request request = buildRequest(url);

        return execute(request, TvEpisode.class);
    }
}
