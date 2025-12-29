package com.video.api.metadata.exception;

import lombok.Getter;
import okhttp3.Response;

@Getter
public class TMDBResponseException extends RuntimeException {

    private final Response response;

    public TMDBResponseException(Response response) {
        this.response = response;
    }
}
