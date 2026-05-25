package com.video.api.video.service;

import org.springframework.core.io.Resource;

import java.util.concurrent.CompletableFuture;

public interface VideoService {
    Resource getVideoMasterPlaylist(Long videoId);
    CompletableFuture<Resource> getVideoPlaylist(Long videoId, String quality);
    CompletableFuture<Resource> getVideoSegment(Long videoId, String quality, Integer segmentNumber);
}
