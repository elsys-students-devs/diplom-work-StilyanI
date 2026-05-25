package com.video.api.video;

import com.video.api.video.config.VideoProperties;
import com.video.api.video.service.TranscodingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class TranscodingServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void isSegmentReadyReturnsWhetherFileExists() throws Exception {
        TranscodingService service = new TranscodingService(videoProperties());
        Path segment = tempDir.resolve("segment000.ts");

        assertThat(service.isSegmentReady(segment)).isFalse();

        Files.writeString(segment, "data");

        assertThat(service.isSegmentReady(segment)).isTrue();
    }

    @Test
    void hasJobFailedReturnsFalseWhenJobIsMissing() {
        TranscodingService service = new TranscodingService(videoProperties());

        assertThat(service.hasJobFailed(tempDir.resolve("movie.mp4"), "720p")).isFalse();
    }

    private VideoProperties videoProperties() {
        VideoProperties properties = new VideoProperties();
        properties.setHlsOutputPath(tempDir.resolve("hls-output").toString());
        properties.setSegmentDuration(10);
        properties.setSeekThresholdSegments(2);
        properties.setStoragePath(tempDir.toString());
        return properties;
    }
}