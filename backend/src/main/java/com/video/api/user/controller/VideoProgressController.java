package com.video.api.user.controller;

import com.video.api.user.dto.VideoProgressDto;
import com.video.api.user.service.VideoProgressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users/video-progress")
public class VideoProgressController {
    private final VideoProgressService videoProgressService;

    public VideoProgressController(VideoProgressService videoProgressService) {
        this.videoProgressService = videoProgressService;
    }

    @GetMapping
    public ResponseEntity<VideoProgressDto> getVideoProgress(@RequestParam UUID userId, @RequestParam String videoId) {
        return ResponseEntity.ok(videoProgressService.getProgress(userId, videoId));
    }

    @PutMapping
    public ResponseEntity<VideoProgressDto> saveProgress(@Valid @RequestBody VideoProgressDto videoProgressDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(videoProgressService.saveProgress(videoProgressDto));
    }
}
