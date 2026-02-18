package com.video.api.video.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShowSeasonFolder {
    private Integer seasonNumber;
    private List<ShowEpisodeFile> availableEpisodes;
}
