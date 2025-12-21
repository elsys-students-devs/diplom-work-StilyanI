package com.video.api.metadata.service;

import com.video.api.metadata.model.Media;
import com.video.api.metadata.model.MediaType;

public interface TMDBService {
    Media search(String name, MediaType mediaType);
}
