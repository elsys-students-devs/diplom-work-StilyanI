package com.video.api.metadata.dto;

import com.video.api.metadata.model.TvEpisode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ShowSeasonDto {
    private int seasonNumber;
    private List<TvEpisode> episodes;
}
