package com.video.api.video.service;

import com.video.api.video.config.VideoProperties;
import com.video.api.video.model.StreamQuality;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import static com.video.api.video.util.VideoUtils.scanHighestSegment;

@Slf4j
@Service
public class TranscodingService {
    private final VideoProperties videoProperties;

    public TranscodingService(VideoProperties videoProperties) {
        this.videoProperties = videoProperties;
    }

    private static class TranscodeJob {
        final Process process;
        final int startSegment;
        volatile int highestSegmentOnDisk;
        volatile boolean failed = false;
        long requestTime;

        TranscodeJob(Process process, int startSegment, int highestSegmentOnDisk, long requestTime) {
            this.process = process;
            this.startSegment = startSegment;
            this.highestSegmentOnDisk = highestSegmentOnDisk;
            this.requestTime = requestTime;
        }
    }

    private final ConcurrentHashMap<String, TranscodeJob> jobMap = new ConcurrentHashMap<>();

    public void ensureTranscoding(Path sourcePath, Path outputDir, StreamQuality profile, int requestedSegment) {
        String key = jobKey(sourcePath, profile);
        jobMap.compute(key, (_, existing) -> {
            if (existing != null && !existing.failed && existing.process.isAlive()) {
                existing.highestSegmentOnDisk = scanHighestSegment(outputDir, videoProperties.getSeekThresholdSegments());

                if (existing.requestTime < System.currentTimeMillis() - 5_000 && ((requestedSegment > existing.highestSegmentOnDisk + videoProperties.getSeekThresholdSegments() && requestedSegment > existing.startSegment + videoProperties.getSeekThresholdSegments()) || requestedSegment < existing.startSegment)) {
                    log.info("Seek detected: key={}, highestOnDisk={}, requested={}", key, existing.highestSegmentOnDisk, requestedSegment);
                    killProcess(key);
                    return launchFfmpeg(sourcePath, outputDir, profile, requestedSegment);
                } else {
                    return existing;
                }
            }

            return launchFfmpeg(sourcePath, outputDir, profile, requestedSegment);
        });
    }

    public boolean isSegmentReady(Path segmentPath) {
        return Files.exists(segmentPath);
    }

    public boolean hasJobFailed(Path sourcePath, String qualityName) {
        TranscodeJob job = jobMap.get(sourcePath.getFileName() + ":" + qualityName);
        return job != null && job.failed;
    }

    private void killProcess(String key) {
        TranscodeJob job = jobMap.get(key);

        try (OutputStream os = job.process.getOutputStream()) {
            os.write("q\n".getBytes());
            os.flush();
        } catch (IOException e) {
            log.warn("Failed to send quit to process", e);
        }

        try {
            if (!job.process.waitFor(5, TimeUnit.SECONDS)) {
                log.warn("Force killing job key={}", key);
                job.process.destroyForcibly();
                job.process.waitFor();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while stopping job key={}", key, e);
        }

        log.info("Killed job key={}", key);
    }

    private TranscodeJob launchFfmpeg(Path sourcePath, Path outputDir, StreamQuality profile, int startSegment) {
        try {
            Files.createDirectories(outputDir);

            double seekSeconds = (double) startSegment * videoProperties.getSegmentDuration();

            List<String> cmd = new ArrayList<>();
            cmd.add("ffmpeg");
            cmd.add("-y");

            cmd.add("-loglevel"); cmd.add("error");

            if (startSegment > 0) {
                cmd.add("-ss");
                cmd.add(String.valueOf(seekSeconds));
            }

            cmd.add("-i");
            cmd.add(sourcePath.toString());

            cmd.add("-c:v"); cmd.add("libx264");
            cmd.add("-preset"); cmd.add("ultrafast");
            cmd.add("-tune"); cmd.add("zerolatency");
            cmd.add("-b:v"); cmd.add(profile.videoBitrate());
            cmd.add("-maxrate"); cmd.add(profile.videoBitrate());
            cmd.add("-bufsize"); cmd.add(profile.videoBitrate());
            cmd.add("-vf"); cmd.add("scale=" + profile.width() + ":" + profile.height());

            cmd.add("-c:a"); cmd.add("aac");
            cmd.add("-b:a"); cmd.add(profile.audioBitrate());

            cmd.add("-f"); cmd.add("hls");
            cmd.add("-hls_time"); cmd.add(String.valueOf(videoProperties.getSegmentDuration()));
            cmd.add("-hls_list_size"); cmd.add("0");
            cmd.add("-hls_segment_filename"); cmd.add(outputDir.resolve("segment%03d.ts").toString());

            if (startSegment > 0) {
                cmd.add("-start_number");
                cmd.add(String.valueOf(startSegment));
            }

            cmd.add(outputDir.resolve("playlist.m3u8").toString());

            log.info("FFmpeg command: {}", String.join(" ", cmd));

            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.redirectErrorStream(true);
            Process proc = pb.start();

            int currentHighest = scanHighestSegment(outputDir, videoProperties.getSeekThresholdSegments());

            String jobKey = jobKey(sourcePath, profile);
            Thread.startVirtualThread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        log.error("[FFmpeg:{}] {}", jobKey, line);
                    }
                } catch (Exception _) {
                    log.error("Couldn't read error from FFmpeg process key={}", jobKey);
                }

                try {
                    int exit = proc.waitFor();
                    if (exit != 0) {
                        log.error("FFmpeg exited with code {} for key={}", exit, jobKey);
                        jobMap.computeIfPresent(jobKey, (_, job) -> { job.failed = true; return job; });
                    } else {
                        log.info("FFmpeg completed successfully for key={}", jobKey);
                    }
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                }
            });

            log.info("Started FFmpeg: key={}, startSegment={}, seekTo={}s", jobKey, startSegment, seekSeconds);
            return new TranscodeJob(proc, startSegment, currentHighest, System.currentTimeMillis());

        } catch (Exception _) {
            log.error("Failed to start FFmpeg for source={}, quality={}", sourcePath, profile.name());
            return null;
        }
    }

    private static String jobKey(Path sourcePath, StreamQuality profile) {
        return sourcePath.getFileName() + ":" + profile.name();
    }

    @PreDestroy
    public void cleanup() throws IOException {
        log.info("Killing active FFmpeg processes...");
        jobMap.forEach((key, job) -> {
            if (job.process.isAlive()) killProcess(key);
        });
        log.info("Finished killing FFmpeg processes");

        log.info("Cleaning up HLS output files...");
        FileUtils.cleanDirectory(Paths.get(videoProperties.getHlsOutputPath()).toFile());
        log.info("Successfully cleaned up HLS output files");
    }
}