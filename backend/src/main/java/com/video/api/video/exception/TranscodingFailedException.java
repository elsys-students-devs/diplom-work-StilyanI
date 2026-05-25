package com.video.api.video.exception;

public class TranscodingFailedException extends RuntimeException {
    public TranscodingFailedException(String message) {
        super(message);
    }
}
