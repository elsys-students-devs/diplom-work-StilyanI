package com.video.api.metadata.model;

import java.util.List;

public record TMDBSearchIdResponse(
        List<Media> movie_results,
        List<Object> person_results,
        List<Media> tv_results,
        List<Object> tv_episode_results,
        List<Object> tv_season_results
) { }
