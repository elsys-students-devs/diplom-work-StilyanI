package com.video.api.metadata.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {

    private final String apiAccessToken;

    public AuthInterceptor(String apiAccessToken) {
        this.apiAccessToken = apiAccessToken;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        Request requestWithBearer = request.newBuilder()
                .addHeader("Authorization", "Bearer " + apiAccessToken)
                .build();

        return chain.proceed(requestWithBearer);
    }
}
