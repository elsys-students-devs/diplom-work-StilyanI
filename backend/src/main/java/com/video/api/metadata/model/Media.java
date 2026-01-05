package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.io.Serializable;
import java.util.List;

public record Media(
        boolean adult,
        @JsonAlias("backdrop_path") String backdropPath,
        @JsonAlias("genre_ids") List<Integer> genreIds,
        int id,
        @JsonAlias("original_language") String originalLanguage,
        @JsonAlias({"original_title", "original_name"}) String originalName,
        String overview,
        double popularity,
        @JsonAlias("poster_path") String posterPath,
        @JsonAlias({"release_date", "first_air_date"}) String releaseDate,
        @JsonAlias({"title", "name"}) String name,
        @JsonAlias("vote_average") double voteAverage,
        @JsonAlias("vote_count") int voteCount
) implements Serializable { }
