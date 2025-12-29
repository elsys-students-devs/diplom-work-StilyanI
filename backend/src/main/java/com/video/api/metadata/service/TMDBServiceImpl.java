package com.video.api.metadata.service;

import com.video.api.metadata.exception.TMDBResponseException;
import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TMDBSearchIdResponse;
import com.video.api.metadata.model.TMDBSearchResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Slf4j
@Service
public class TMDBServiceImpl implements TMDBService {

    @Autowired private OkHttpClient client;
    @Autowired private ObjectMapper objectMapper;

    @Override
    public Media search(String name, MediaType mediaType, String otherParameters) {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/search/" + mediaType.getType() + "?query=" + name + "&include_adult=false&language=en-US&page=1" + (otherParameters != null ? "&" + otherParameters : ""))
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful())throw new TMDBResponseException(response);

            TMDBSearchResponse tmdbSearchResponse = objectMapper.readValue(response.body().string(), TMDBSearchResponse.class);

            return tmdbSearchResponse.results().getFirst();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Media search(String name, MediaType mediaType) {
        return search(name, mediaType, "");
    }

    @Override
    public Media search(String name, MediaType mediaType, Integer year) {
        if(year == null)
            return search(name, mediaType);
        else
            return search(name, mediaType, "year=" + year);
    }

    @Override
    public Media findById(String id, String source) {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/find/" + id + "?external_source=" + source + "&language=en-US")
                .get()
                .addHeader("accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(!response.isSuccessful())throw new TMDBResponseException(response);

            TMDBSearchIdResponse tmdbSearchIdResponse = objectMapper.readValue(response.body().string(), TMDBSearchIdResponse.class);

            if(!tmdbSearchIdResponse.movie_results().isEmpty()) return tmdbSearchIdResponse.movie_results().getFirst();
            else if(!tmdbSearchIdResponse.tv_results().isEmpty()) return tmdbSearchIdResponse.tv_results().getFirst();
            else return null;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
