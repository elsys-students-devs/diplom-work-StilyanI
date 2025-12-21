package com.video.api.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MediaType {
    TVSHOW("tv"),
    MOVIE("movie");

    @Getter
    private final String type;
}
