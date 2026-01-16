package com.video.api.video.controller;

import com.video.api.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoController {
    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, List<Map<String, String>>>>  getFoldersList() {
        return ResponseEntity.ok(videoService.getFoldersList());
    }

    @GetMapping("/{fileName}/master.m3u8")
    public ResponseEntity<Resource> getVideoPlaylist(@PathVariable String fileName) {
        return videoService.getVideoPlaylist(fileName);
    }

    @GetMapping("/{fileName}/{segment}.ts")
    public ResponseEntity<Resource> getVideoPlaylist(@PathVariable String fileName, @PathVariable String segment) {
        return videoService.getVideoSegment(fileName, segment);
    }
}
