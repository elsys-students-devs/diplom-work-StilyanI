package com.video.api.video.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    private void segmentizeVideo(String fileName) {
        //TODO
    }

    @Override
    public ResponseEntity<Resource> getVideoPlaylist(String fileName) {
        Path path = Paths.get(hlsDirectory, fileName, "master.m3u8");
        if(!Files.exists(path)){
            segmentizeVideo(fileName);
        }

        Resource playlistResource = new FileSystemResource(path);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,"application/vnd.apple.mpegurl").body(playlistResource);
    }

    @Override
    public ResponseEntity<Resource> getVideoSegment(String fileName, String segment) {
        Path path = Paths.get(hlsDirectory, fileName, segment + ".ts");
        if(!Files.exists(path)){
            segmentizeVideo(fileName);
        }

        Resource segmentResource = new FileSystemResource(path);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "video/MP2T").body(segmentResource);
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
