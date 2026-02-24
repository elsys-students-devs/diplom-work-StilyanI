package com.video.api.video.service;

import com.video.api.video.config.VideoProperties;
import com.video.api.video.exception.InvalidStreamQualityException;
import com.video.api.video.exception.PlaylistFailedCreationException;
import com.video.api.video.exception.TranscodingFailedException;
import com.video.api.video.exception.VideoFileNotFoundException;
import com.video.api.video.model.StreamQuality;
import com.video.api.video.model.Video;
import com.video.api.video.repository.VideoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static com.video.api.video.util.VideoUtils.getVideoDuration;
import static com.video.api.video.util.VideoUtils.scanHighestSegment;
import static com.video.api.video.util.VideoUtils.segmentPath;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {
    private static final int POLL_INTERVAL_MS = 200;

    private static final int SEGMENT_TIMEOUT_MS = 30_000;

    private final TranscodingService transcodingService;
    private final VideoProperties videoProperties;
    private final VideoRepository videoRepository;

    public VideoServiceImpl(TranscodingService transcodingService, VideoProperties videoProperties, VideoRepository videoRepository) {
        this.transcodingService = transcodingService;
        this.videoProperties = videoProperties;
        this.videoRepository = videoRepository;
    }

    @Override
    public Resource getVideoMasterPlaylist(Long videoId) {
        log.info("Getting video master playlist for video id {}", videoId);

        Path masterPath = Paths.get(videoProperties.getHlsOutputPath(), String.valueOf(videoId), "master.m3u8");

        if(!Files.exists(masterPath)) {
            String masterContent = StreamQuality.QUALITY_LIST.stream()
                    .map(p -> {
                        try {
                            int videoBandwidthKbps = Integer.parseInt(p.videoBitrate().replace("k", ""));
                            int audioBandwidthKbps = Integer.parseInt(p.audioBitrate().replace("k", ""));
                            int totalBandwidth = (videoBandwidthKbps + audioBandwidthKbps) * 1000;

                            return "#EXT-X-STREAM-INF:BANDWIDTH=" + totalBandwidth
                                    + ",RESOLUTION=" + p.width() + "x" + p.height() + "\n"
                                    + p.name() + "/playlist.m3u8";
                        }catch (NumberFormatException _) {
                            throw new PlaylistFailedCreationException("Failed to parse bitrate while creating master playlist");
                        }
                    })
                    .collect(Collectors.joining("\n", "#EXTM3U\n", "\n"));

            try {
                Files.createDirectories(masterPath.getParent());
                Files.writeString(masterPath, masterContent);
            } catch (Exception _) {
                throw new PlaylistFailedCreationException("Failed to create master playlist for videoId=" + videoId);
            }
        }

        return new FileSystemResource(masterPath);
    }

    @Override
    public Resource getVideoPlaylist(Long videoId, String quality) {
        Path sourcePath = resolveSourceVideo(videoId);
        StreamQuality streamQuality = StreamQuality.byName(quality);

        verifyRequestParams(videoId, quality, sourcePath, streamQuality);

        Path outputDir = Paths.get(videoProperties.getHlsOutputPath(), String.valueOf(videoId), quality);
        Path playlistPath = outputDir.resolve("playlist-temp.m3u8");

        if(!Files.exists(playlistPath)) {
            transcodingService.ensureTranscoding(sourcePath, outputDir, streamQuality, 0);

            long deadline = System.currentTimeMillis() + SEGMENT_TIMEOUT_MS;
            while (scanHighestSegment(outputDir, videoProperties.getSeekThresholdSegments()) < 0) {

                if (transcodingService.hasJobFailed(sourcePath, quality)) {
                    throw new TranscodingFailedException("Transcoding failed for videoId=" + videoId);
                }
                if (System.currentTimeMillis() > deadline) {
                    throw new TranscodingFailedException("Transcoding timed out for videoId=" + videoId);
                }
                sleep();
            }

            try {
                String playlistContent = buildPlaylist(sourcePath);
                Files.writeString(playlistPath, playlistContent);
            } catch (Exception _) {
                throw new PlaylistFailedCreationException("Failed to create variant playlist for videoId=" + videoId + ", quality=" + quality);
            }
        }

        return new FileSystemResource(playlistPath);
    }

    @Override
    public Resource getVideoSegment(Long videoId, String quality, Integer segmentNumber) {
        Path sourcePath = resolveSourceVideo(videoId);
        StreamQuality streamQuality = StreamQuality.byName(quality);

        verifyRequestParams(videoId, quality, sourcePath, streamQuality);

        Path outputDir = Paths.get(videoProperties.getHlsOutputPath(), String.valueOf(videoId), quality);
        Path segmentPath = segmentPath(outputDir, segmentNumber);

        if(!Files.exists(segmentPath)) {
            transcodingService.ensureTranscoding(sourcePath, outputDir, streamQuality, segmentNumber);

            long deadline = System.currentTimeMillis() + SEGMENT_TIMEOUT_MS;
            while (!transcodingService.isSegmentReady(segmentPath)) {
                if (transcodingService.hasJobFailed(sourcePath, quality)) {
                    throw new TranscodingFailedException("Transcoding failed for videoId=" + videoId);
                }
                if (System.currentTimeMillis() > deadline) {
                    throw new TranscodingFailedException("Transcoding timed out for videoId=" + videoId);
                }
                sleep();
            }
        }

        return new FileSystemResource(segmentPath);
    }

    private void verifyRequestParams(Long videoId, String quality, Path sourcePath, StreamQuality sq){
        if (sourcePath == null) {
            throw new VideoFileNotFoundException("Source video not found for videoId=" + videoId);
        }
        if(sq == null) {
            throw new InvalidStreamQualityException("Given stream quality(" + quality + ") is not valid. Available stream qualities are: " + StreamQuality.QUALITY_LIST.stream().map(StreamQuality::name).toList());
        }
    }

    private Path resolveSourceVideo(Long videoId) {
        Video video = videoRepository.findById(videoId).orElseThrow(() -> new VideoFileNotFoundException("Source video not found for videoId=" + videoId));
        return Paths.get(video.getPathToVideo());
    }

    private String buildPlaylist(Path sourcePath){

        double duration = getVideoDuration(sourcePath);
        int numOfSegments = (int) Math.ceil(duration / videoProperties.getSegmentDuration());

        StringBuilder sb = new StringBuilder();
        sb.append("#EXTM3U\n");
        sb.append("#EXT-X-VERSION:3\n");
        sb.append("#EXT-X-TARGETDURATION:").append(videoProperties.getSegmentDuration()).append("\n");
        sb.append("#EXT-X-MEDIA-SEQUENCE:0\n");
        sb.append("#EXT-X-PLAYLIST-TYPE:VOD\n");

        for (int i = 0; i < numOfSegments; i++) {
            sb.append("#EXTINF:");
            if(i == numOfSegments - 1) {
                sb.append(duration - i * videoProperties.getSegmentDuration()).append(",\n");
            } else {
                sb.append(videoProperties.getSegmentDuration()).append(".0,\n");
            }
            sb.append(String.format("segment%03d.ts%n", i));
        }

        sb.append("#EXT-X-ENDLIST\n");
        return sb.toString();
    }

    private static void sleep() {
        try {
            Thread.sleep(VideoServiceImpl.POLL_INTERVAL_MS);
        } catch (InterruptedException _) {
            Thread.currentThread().interrupt();
        }
    }
}
