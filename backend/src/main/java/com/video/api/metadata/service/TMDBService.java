package com.video.api.metadata.service;

import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaIdSource;
import com.video.api.metadata.model.MediaType;
import com.video.api.metadata.model.TvEpisode;

import java.util.HashMap;

public interface TMDBService {
    Media search(String name, MediaType mediaType, HashMap<String, String> otherParameters);
    Media search(String name, MediaType mediaType);
    Media search(String name, MediaType mediaType, Integer year);

    Media findById(String id, MediaIdSource source);

    TvEpisode getTvEpisode(int seriesId, int seasonNumber, int episodeNumber);
}
