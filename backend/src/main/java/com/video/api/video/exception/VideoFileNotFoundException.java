package com.video.api.video.exception;

public class VideoFileNotFoundException extends RuntimeException {
    public VideoFileNotFoundException(String message) {
        super(message);
    }
}
