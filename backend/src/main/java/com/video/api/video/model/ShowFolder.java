package com.video.api.video.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class ShowFolder {
    private final String title;
    private final Integer year;
    private final String imdbId;

    private final List<ShowSeasonFolder> availableSeasons;
    private int tmdbId;
}
