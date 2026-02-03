package com.video.api.video.util;

import com.video.api.video.exception.TranscodingFailedException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VideoUtils {

    private VideoUtils() {}

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
            throw new TranscodingFailedException(e.getMessage());
        }

        return Double.parseDouble(output);
    }

    public static int scanHighestSegment(Path outputDir, int seekThresholdSegments) {
        int highest = -1;
        for (int i = 0; ; i++) {
            if (Files.exists(segmentPath(outputDir, i))) {
                highest = i;
            } else {
                if(i > highest + seekThresholdSegments + 10) break;
            }
        }
        return highest;
    }

    public static Path segmentPath(Path outputDir, int index) {
        return outputDir.resolve(String.format("segment%03d.ts", index));
    }
}
