package com.video.api.metadata.controller;

import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TvEpisode;
import com.video.api.metadata.service.TMDBService;
import com.video.api.metadata.model.Media;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("metadata")
public class MetadataController {
    private final TMDBService tmdbService;

    public MetadataController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> findById(@PathVariable("id") String id, @RequestParam String source){
        Media result = tmdbService.findById(id, source);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/tv")
    public ResponseEntity<Media> searchTv(@RequestParam String name, @RequestParam(required = false) Integer year) {
        Media result = tmdbService.search(name, MediaType.TVSHOW, year);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/movie")
    public ResponseEntity<Media> searchMovie(@RequestParam String name, @RequestParam(required = false) Integer year) {
        Media result = tmdbService.search(name, MediaType.MOVIE, year);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tv/{seriesId}/season/{seasonId}/episode/{episodeId}")
    public ResponseEntity<TvEpisode> getTvEpisode(@PathVariable("seriesId") int seriesId, @PathVariable("seasonId") int seasonNumber, @PathVariable("episodeId") int episodeNumber){
        TvEpisode result = tmdbService.getTvEpisode(seriesId, seasonNumber, episodeNumber);
        return ResponseEntity.ok(result);
    }
}
