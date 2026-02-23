package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TvEpisode implements Serializable {
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String airDate;

    @JsonAlias("episode_number")
    private int episodeNumber;

    private String name;

    private String overview;

    @JsonAlias("id")
    private int tmdbId;

    private int runtime;

    @JsonAlias("season_number")
    private int seasonNumber;

    @JsonAlias("still_path")
    private String stillPath;
}
