package com.video.api.video.service;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VideoService {
    ResponseEntity<ResourceRegion> getVideoByFileName(String fileName, HttpHeaders headers);
    Map<String, List<Map<String, String>>> getFoldersList();
}
