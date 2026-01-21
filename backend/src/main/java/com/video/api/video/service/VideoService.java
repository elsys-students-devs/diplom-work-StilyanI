package com.video.api.video.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface VideoService {
    ResponseEntity<Resource> getVideoPlaylist(String videoId);
    ResponseEntity<Resource> getVideoHlsFile(String videoId, String quality, String hlsFileName);
    Map<String, List<Map<String, String>>> getFoldersList();
}
