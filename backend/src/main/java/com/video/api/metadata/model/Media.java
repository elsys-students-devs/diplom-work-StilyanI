package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record Media(
        boolean adult,
        String backdrop_path,
        List<Integer> genre_ids,
        int id,
        String original_language,
        @JsonAlias({"original_title", "original_name"}) String original_name,
        String overview,
        double popularity,
        String poster_path,
        @JsonAlias({"release_date", "first_air_date"}) String release_date,
        @JsonAlias({"title", "name"}) String name,
        double vote_average,
        int vote_count
) { }
