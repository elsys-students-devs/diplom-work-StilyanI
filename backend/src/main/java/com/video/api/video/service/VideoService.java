package com.video.api.video.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VideoService {
    ResponseEntity<Resource> getVideoPlaylist(String fileName);
    ResponseEntity<Resource> getVideoSegment(String fileName, String segment);
    Map<String, List<Map<String, String>>> getFoldersList();
}
