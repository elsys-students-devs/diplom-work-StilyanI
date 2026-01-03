package com.video.api.metadata.config;

import com.video.api.metadata.interceptor.AuthInterceptor;
import com.video.api.metadata.interceptor.RetryInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

@Configuration

public class HttpClientConfiguration {

    @Value("${tmdb.api.access.token}")
    private String apiAccessToken;

    @Bean
    public OkHttpClient httpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(apiAccessToken))
                .addInterceptor(new RetryInterceptor())
                .build();
    }

    @Bean
    public JsonMapper objectMapper(){
        return JsonMapper.builder()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }
}
