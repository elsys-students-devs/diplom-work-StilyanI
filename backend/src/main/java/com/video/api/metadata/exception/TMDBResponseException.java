package com.video.api.metadata.exception;

import lombok.Getter;
import okhttp3.Response;

@Getter
public class TMDBResponseException extends RuntimeException {

    private final Response response;

    public TMDBResponseException(String message, Response response) {
        super(message);
        this.response = response;
    }
}
