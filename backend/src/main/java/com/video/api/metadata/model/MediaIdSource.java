package com.video.api.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum MediaIdSource {
    IMDB("imdb_id"),
    TVDB("tvdb_id"),
    WIKIDATA("wikidata_id");

    @Getter
    private final String source;
}
