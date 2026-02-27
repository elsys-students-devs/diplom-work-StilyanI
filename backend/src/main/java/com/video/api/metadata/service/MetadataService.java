package com.video.api.metadata.service;

import com.video.api.metadata.dto.ShowSeasonDto;
import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TvEpisode;

import java.util.List;
import java.util.Map;

public interface MetadataService {
    Media[] getAll();

    Media search(String name, MediaType mediaType, Map<String, String> otherParameters);
    Media search(String name, MediaType mediaType);
    Media search(String name, MediaType mediaType, Integer year);

    Media findById(String id);

    List<ShowSeasonDto> getTvSeasons(int tmdbId);
    TvEpisode getTvEpisode(int seriesId, int seasonNumber, int episodeNumber);
}
