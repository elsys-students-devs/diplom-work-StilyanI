package com.video.api.video.util;

import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VideoUtils {
    @Value("${video.segment-duration}")
    private static int segmentDuration;

    public static double getVideoDuration(Path sourcePath){
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "csv=p=0",
                sourcePath.toString()
        );

        String output;
        try {
            Process process = pb.start();
            output = new String(process.getInputStream().readAllBytes()).trim();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Double.parseDouble(output);
    }

    public static int scanHighestSegment(Path outputDir) {
        int highest = -1;
        for (int i = 0; ; i++) {
            if (Files.exists(segmentPath(outputDir, i))) {
                highest = i;
            } else {
                if(i > highest + segmentDuration + 10) break;
            }
        }
        return highest;
    }

    public static Path segmentPath(Path outputDir, int index) {
        return outputDir.resolve(String.format("segment%03d.ts", index));
    }
}
