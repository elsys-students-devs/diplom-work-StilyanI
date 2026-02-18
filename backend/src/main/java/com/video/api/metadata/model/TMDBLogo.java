package com.video.api.metadata.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record TMDBLogo(
        @JsonAlias("file_path") String filePath,
        @JsonAlias("iso_639_1") String iso6391,
        @JsonAlias("vote_average") double voteAverage
) {}
