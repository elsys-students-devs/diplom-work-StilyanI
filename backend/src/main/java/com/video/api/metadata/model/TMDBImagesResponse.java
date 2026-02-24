package com.video.api.metadata.model;

import java.util.List;

public record TMDBImagesResponse(
        List<TMDBLogo> logos
) {}
