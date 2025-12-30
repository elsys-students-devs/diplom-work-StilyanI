package com.video.api.metadata.model;

public record TvEpisode(
        String air_date,
        int episode_number,
        String name,
        String overview,
        int id,
        int runtime,
        int season_number,
        String still_path
) { }
