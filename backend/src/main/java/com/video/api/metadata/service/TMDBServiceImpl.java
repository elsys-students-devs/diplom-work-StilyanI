package com.video.api.metadata.service;

import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TMDBSearchIdResponse;
import com.video.api.metadata.model.TMDBSearchResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Service
public class TMDBServiceImpl implements TMDBService {

    @Value("${tmdb.api.access.token}")
    private String apiAccessToken;

    OkHttpClient client = new OkHttpClient();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Media search(String name, MediaType mediaType, String otherParameters) {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/search/" + mediaType.getType() + "?query=" + name + "&include_adult=false&language=en-US&page=1" + (otherParameters != null ? "&" + otherParameters : ""))
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiAccessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            TMDBSearchResponse tmdbSearchResponse = objectMapper.readValue(response.body().string(), TMDBSearchResponse.class);

            return tmdbSearchResponse.results().getFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Media search(String name, MediaType mediaType) {
        return search(name, mediaType, null);
    }

    @Override
    public Media search(String name, MediaType mediaType, int year) {
        return search(name, mediaType, "year=" + year);
    }

    @Override
    public Media findById(String id, String source) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/find/" + id + "?external_source=" + source + "&language=en-US")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiAccessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            TMDBSearchIdResponse tmdbSearchIdResponse = objectMapper.readValue(response.body().string(), TMDBSearchIdResponse.class);
            if(!tmdbSearchIdResponse.movie_results().isEmpty())
                return tmdbSearchIdResponse.movie_results().getFirst();
            else
                return tmdbSearchIdResponse.tv_results().getFirst();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
