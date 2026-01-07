package com.video.api.video.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface VideoService {
    ResponseEntity<StreamingResponseBody> getVideoByPath(String path, HttpHeaders headers);
}
