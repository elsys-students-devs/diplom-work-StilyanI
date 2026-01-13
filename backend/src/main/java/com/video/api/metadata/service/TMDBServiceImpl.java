package com.video.api.metadata.service;

import com.video.api.common.exception.TMDBResponseException;
import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaIdSource;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TMDBSearchIdResponse;
import com.video.api.metadata.model.TMDBSearchResponse;
import com.video.api.metadata.model.TvEpisode;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@Service
public class TMDBServiceImpl implements TMDBService {

    @Autowired private final OkHttpClient client;
    @Autowired private JsonMapper jsonMapper;
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    public TMDBServiceImpl(OkHttpClient client) {
        this.client = client;
    }

    private <T> T execute(Request request, Class<T> responseType) {
        try (Response response = client.newCall(request).execute()) {
            String body = response.body().string();

            if (!response.isSuccessful()) {
                throw new TMDBResponseException(body, response);
            }

            return jsonMapper.readValue(body, responseType);

        } catch (IOException e) {
            throw new RuntimeException("TMDB request failed", e);
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
    public Media search(String name, MediaType mediaType, HashMap<String, String> otherParameters) {
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder()
                .addPathSegment("search")
                .addPathSegment(mediaType.getType())
                .addQueryParameter("query", name)
                .addQueryParameter("include_adult", "false")
                .addQueryParameter("language", "en-US")
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
            key = "#id + '-' + #source"
    )
    @Override
    public Media findById(String id, MediaIdSource source) {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse(BASE_URL)).newBuilder()
                .addPathSegment("find")
                .addPathSegment(id)
                .addQueryParameter("external_source", source.getSource())
                .addQueryParameter("language", "en-US")
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
                .addQueryParameter("language", "en-US")
                .build();

        Request request = buildRequest(url);

        return execute(request, TvEpisode.class);
    }
}
