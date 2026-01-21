package com.video.api.video.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.video.api.video.util.FileBrowsingUtils.parseDirectories;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Value("${video.storage.path}")
    private String videoDirectory;

    @Value("${hls.storage.path}")
    private String hlsDirectory;

    public void segmentizeVideo(String fileName) {
        Path videoPath = Paths.get(videoDirectory, fileName);
        try{
            Path outputPath = Paths.get(hlsDirectory, fileName.substring(0, fileName.lastIndexOf('.')));
            Files.createDirectories(outputPath);

            for (int i = 0; i < 3; i++) {
                Files.createDirectories(outputPath.resolve("v" + i));
            }

            String cmd = "ffmpeg " +
                    "-i " + videoPath.toString().replace("\\", "/") + " " +

                    "-map 0:v:0 -map 0:a:0 " +
                    "-s:v:0 1920x1080 " +
                    "-c:v:0 libx264 " +
                    "-b:v:0 5000k " +
                    "-c:a:0 aac " +
                    "-b:a:0 192k " +

                    "-map 0:v:0 -map 0:a:0 " +
                    "-s:v:1 1280x720 " +
                    "-c:v:1 libx264 " +
                    "-b:v:1 2800k " +
                    "-c:a:1 aac " +
                    "-b:a:1 128k " +

                    "-map 0:v:0 -map 0:a:0 " +
                    "-s:v:2 640x360 " +
                    "-c:v:2 libx264 " +
                    "-b:v:2 800k " +
                    "-c:a:2 aac " +
                    "-b:a:2 96k " +

                    "-f hls " +
                    "-hls_time 6 " +
                    "-hls_playlist_type vod " +
                    "-hls_segment_filename " + outputPath.toString().replace("\\", "/") + "/v%v/segment%03d.ts" + " " +
                    "-master_pl_name master.m3u8 " +
                    "-var_stream_map \"v:0,a:0 v:1,a:1 v:2,a:2\" " +
                    outputPath.toString().replace("\\", "/") + "/v%v/playlist.m3u8";

            ProcessBuilder processBuilder;
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                processBuilder = new ProcessBuilder("cmd.exe", "/c", cmd);
            } else {
                processBuilder = new ProcessBuilder("/bin/bash", "-c", cmd);
            }
            processBuilder.inheritIO();

            log.info("Starting video processing...");
            Process process = processBuilder.start();
            log.info("Ended video process.");

            int exit = process.waitFor();
            if (exit != 0) {
                throw new RuntimeException("video processing failed!!");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<Resource> getVideoPlaylist(String videoId) {
        Path path = Paths.get(hlsDirectory, videoId, "master.m3u8");
        if(!Files.exists(path)){
            segmentizeVideo(videoId);
        }

        Resource playlistResource = new FileSystemResource(path);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,"application/vnd.apple.mpegurl").body(playlistResource);
    }

    @Override
    public ResponseEntity<Resource> getVideoHlsFile(String videoId, String quality, String hlsFileName) {
        Path path = Paths.get(hlsDirectory, videoId, "v" + quality, hlsFileName);

        Resource hlsResource = new FileSystemResource(path);

        MediaType mediaType = hlsFileName.endsWith(".m3u8")
                ? MediaType.parseMediaType("application/vnd.apple.mpegurl")
                : MediaType.parseMediaType("video/mp2t");

        return ResponseEntity.ok().contentType(mediaType).body(hlsResource);
    }

    @Override
    public Map<String, List<Map<String, String>>> getFoldersList() {
        List<Map<String, String>> parsedMovieDirectories = parseDirectories(videoDirectory + "/Movies");
        List<Map<String, String>> parsedShowDirectories = parseDirectories(videoDirectory + "/Shows");

        Map<String, List<Map<String, String>>> result = new HashMap<>();
        result.put("Movies", parsedMovieDirectories);
        result.put("Shows", parsedShowDirectories);

        return result;
    }
}
