package com.video.api.metadata.controller;

import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.service.TMDBService;
import com.video.api.metadata.model.Media;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("metadata")
public class MetadataController {
    private final TMDBService tmdbService;

    public MetadataController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/tv")
    public ResponseEntity<Media> searchTv(@RequestParam String name) throws IOException {
        Media result = tmdbService.search(name, MediaType.TVSHOW);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/movie")
    public ResponseEntity<Media> searchMovie(@RequestParam String name) throws IOException {
        Media result = tmdbService.search(name, MediaType.MOVIE);
        return ResponseEntity.ok(result);
    }
}
