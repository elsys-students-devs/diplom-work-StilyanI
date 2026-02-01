package com.video.api.video.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VideoService {
    Map<String, List<Map<String, String>>> getFoldersList();

    ResponseEntity<Resource> getVideoMasterPlaylist(String videoId);
    ResponseEntity<Resource> getVideoPlaylist(String videoId, String quality);
    ResponseEntity<Resource> getVideoSegment(String videoId, String quality, Integer segmentNumber);
}
