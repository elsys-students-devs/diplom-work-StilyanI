package com.video.api.metadata.model;

import java.util.List;

public record TMDBSearchResponse(
        int page,
        List<Media> results,
        int total_pages,
        int total_results
) { }
