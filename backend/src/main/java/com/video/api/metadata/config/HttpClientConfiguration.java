package com.video.api.metadata.config;

import com.video.api.metadata.interceptor.AuthInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration

public class HttpClientConfiguration {

    @Value("${tmdb.api.access.token}")
    private String apiAccessToken;

    @Bean
    public OkHttpClient httpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(apiAccessToken))
                .build();
    }

    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
