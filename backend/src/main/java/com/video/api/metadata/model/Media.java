package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Media implements Serializable {
    @JsonProperty(value = "id", access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private boolean adult;

    @JsonAlias("backdrop_path")
    private String backdropPath;

    @JsonAlias("genre_ids")
    private List<Integer> genreIds;

    @JsonAlias("id")
    private int tmdbId;

    @JsonAlias("original_language")
    private String originalLanguage;

    @JsonAlias({"original_title", "original_name"})
    private String originalName;

    private String overview;

    private double popularity;

    @JsonAlias("poster_path")
    private String posterPath;

    @JsonAlias({"release_date", "first_air_date"})
    private String releaseDate;

    @JsonAlias({"title", "name"})
    private String name;

    @JsonAlias("vote_average")
    private double voteAverage;

    @JsonAlias("vote_count")
    private int voteCount;

    private String logoPath;

    private String type;
}
