package com.video.api.user.exception;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(String message) {
        super(message);
    }
}
