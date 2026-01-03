package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record TMDBSearchIdResponse(
        @JsonAlias("movie_results") List<Media> movieResults,
        @JsonAlias("person_results") List<Object> personResults,
        @JsonAlias("tv_results") List<Media> tvResults,
        @JsonAlias("tv_episode_results") List<Object> tvEpisodeResults,
        @JsonAlias("tv_season_results") List<Object> tvSeasonResults
) { }
