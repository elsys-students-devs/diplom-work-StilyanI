package com.video.api.video.controller;

import com.video.api.video.service.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
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
    public Map<String, List<Map<String, String>>>  getFoldersList() {
        return videoService.getFoldersList();
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<ResourceRegion> getVideoByFileName(@PathVariable String fileName, @RequestHeader HttpHeaders headers) {
        return videoService.getVideoByFileName(fileName, headers);
    }
}
