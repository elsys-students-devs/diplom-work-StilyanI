package com.video.api.video.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MovieFolder {
    private Long id;
    private String title;
    private Integer year;
    private String imdbId;
}
