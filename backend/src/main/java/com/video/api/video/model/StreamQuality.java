package com.video.api.video.model;

import java.util.ArrayList;
import java.util.List;

public record StreamQuality(
        String name,
        int width,
        int height,
        String videoBitrate,
        String audioBitrate
) {
    public static final List<StreamQuality> QUALITY_LIST = new ArrayList<>(List.of(
            new StreamQuality("1080p", 1920, 1080, "5000k", "192k"),
            new StreamQuality("720p",  1280, 720,  "2500k", "128k"),
            new StreamQuality("480p",  854,  480,  "1000k", "64k")
    ));

    public static StreamQuality byName(String name){
        return QUALITY_LIST.stream().filter(q -> q.name().equals(name)).findFirst().orElse(null);
    }
}
