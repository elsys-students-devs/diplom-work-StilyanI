package com.video.api.metadata.controller;

import com.video.api.metadata.dto.ShowSeasonDto;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TvEpisode;
import com.video.api.metadata.service.MetadataService;
import com.video.api.metadata.model.Media;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/metadata")
public class MetadataController {
    private final MetadataService metadataService;

    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping
    public ResponseEntity<Media[]> getAll(){
        return ResponseEntity.ok(metadataService.getAll());
    }

    @GetMapping("/shows/{id}/seasons")
    public ResponseEntity<List<ShowSeasonDto>> getShowSeasons(@PathVariable int id){
        return ResponseEntity.ok(metadataService.getTvSeasons(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Media> findById(@PathVariable String id){
        Media result = metadataService.findById(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/tv")
    public ResponseEntity<Media> searchTv(@RequestParam String name, @RequestParam(required = false) Integer year) {
        Media result = metadataService.search(name, MediaType.TVSHOW, year);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search/movie")
    public ResponseEntity<Media> searchMovie(@RequestParam String name, @RequestParam(required = false) Integer year) {
        Media result = metadataService.search(name, MediaType.MOVIE, year);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/tv/{seriesId}/season/{seasonNumber}/episode/{episodeNumber}")
    public ResponseEntity<TvEpisode> getTvEpisode(@PathVariable int seriesId, @PathVariable int seasonNumber, @PathVariable int episodeNumber){
        TvEpisode result = metadataService.getTvEpisode(seriesId, seasonNumber, episodeNumber);
        return ResponseEntity.ok(result);
    }
}
