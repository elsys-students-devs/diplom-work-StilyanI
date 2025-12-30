package com.video.api.metadata.service;

import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TvEpisode;

public interface TMDBService {
    Media search(String name, MediaType mediaType, String otherParameters);
    Media search(String name, MediaType mediaType);
    Media search(String name, MediaType mediaType, Integer year);

    Media findById(String id, String source);

    TvEpisode getTvEpisode(int seriesId, int seasonNumber, int episodeNumber);
}
