package com.video.api.video.controller;

import com.video.api.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/{videoId}/master.m3u8")
    public ResponseEntity<Resource> getVideoMasterPlaylist(@PathVariable Long videoId) {
        return ResponseEntity.ok()
                .contentType(new MediaType("application", "x-mpegURL"))
                .body(videoService.getVideoMasterPlaylist(videoId));
    }

    @GetMapping("/{videoId}/{quality}/playlist.m3u8")
    public ResponseEntity<Resource> getVideoPlaylist(@PathVariable Long videoId, @PathVariable String quality) {
        return ResponseEntity.ok()
                .contentType(new MediaType("application", "x-mpegURL"))
                .body(videoService.getVideoPlaylist(videoId, quality));
    }

    @GetMapping("/{videoId}/{quality}/segment{segmentNumber}.ts")
    public ResponseEntity<Resource>  getVideoSegment(@PathVariable Long videoId, @PathVariable String quality, @PathVariable Integer segmentNumber) {
        return ResponseEntity.ok()
                .contentType(new MediaType("video", "mp2t"))
                .body(videoService.getVideoSegment(videoId, quality, segmentNumber));
    }
}
