package com.video.api.video.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "video")
@Data
public class VideoProperties {
    String storagePath;
    String hlsOutputPath;
    Integer segmentDuration;
    Integer seekThresholdSegments;
}
