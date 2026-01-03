package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;

public record TvEpisode(
        String airDate,
        @JsonAlias("episode_number") int episodeNumber,
        String name,
        String overview,
        int id,
        int runtime,
        @JsonAlias("season_number") int seasonNumber,
        @JsonAlias("still_path") String stillPath
) implements Serializable { }
