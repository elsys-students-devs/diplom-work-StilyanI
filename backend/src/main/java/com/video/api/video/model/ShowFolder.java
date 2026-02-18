package com.video.api.video.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShowFolder {
    private String title;
    private Integer year;
    private String imdbId;

    private List<ShowSeasonFolder> availableSeasons;
}
