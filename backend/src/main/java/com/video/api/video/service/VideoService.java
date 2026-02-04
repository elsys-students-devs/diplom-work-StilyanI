package com.video.api.video.service;

import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

public interface VideoService {
    Map<String, List<Map<String, String>>> getFoldersList();

    Resource getVideoMasterPlaylist(String videoId);
    Resource getVideoPlaylist(String videoId, String quality);
    Resource getVideoSegment(String videoId, String quality, Integer segmentNumber);
}
