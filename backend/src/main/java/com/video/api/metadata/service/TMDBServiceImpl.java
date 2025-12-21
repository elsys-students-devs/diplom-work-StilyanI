package com.video.api.metadata.service;

import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaType;
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
    public Media search(String name, MediaType mediaType) {
        log.info("Api: " + apiAccessToken);
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/search/" + mediaType.getType() + "?query=" + name + "&include_adult=false&language=en-US&page=1")
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

}
