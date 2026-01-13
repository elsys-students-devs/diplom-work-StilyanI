package com.video.api.video.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.video.api.video.util.FileBrowsingUtils.parseDirectories;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Value("${video.storage.path}")
    private String videoDirectory;

    @Override
    public ResponseEntity<ResourceRegion> getVideoByFileName(String fileName, HttpHeaders headers) {
        return null;
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
