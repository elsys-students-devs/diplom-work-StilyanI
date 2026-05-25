package com.video.api.video.model;

import lombok.Data;

import java.util.List;

@Data
public class FolderSearchResult {
    private List<MovieFolder> movies;
    private List<ShowFolder> shows;
}
