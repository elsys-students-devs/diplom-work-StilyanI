package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record TMDBSearchResponse(
        int page,
        List<Media> results,
        @JsonAlias("total_pages") int totalPages,
        @JsonAlias("total_results") int totalResults
) { }
