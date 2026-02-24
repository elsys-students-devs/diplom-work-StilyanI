package com.video.api.video.service;

import org.springframework.core.io.Resource;

public interface VideoService {
    Resource getVideoMasterPlaylist(Long videoId);
    Resource getVideoPlaylist(Long videoId, String quality);
    Resource getVideoSegment(Long videoId, String quality, Integer segmentNumber);
}
