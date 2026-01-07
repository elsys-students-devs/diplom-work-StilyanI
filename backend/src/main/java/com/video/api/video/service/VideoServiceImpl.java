package com.video.api.video.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Override
    public ResponseEntity<StreamingResponseBody> getVideoByPath(String filePath, HttpHeaders headers) {
        Path videoFilePath = Paths.get(filePath);

        if(!Files.isRegularFile(videoFilePath)){
            throw new RuntimeException("File not found!");
        }

        StreamingResponseBody stream = outputStream -> {
            try {
                final InputStream inputStream = new FileInputStream(videoFilePath.toFile());

                byte[] bytes = new byte[1024];
                int length;
                while ((length = inputStream.read(bytes)) >= 0) {
                    outputStream.write(bytes, 0, length);
                }
                inputStream.close();
                outputStream.flush();

            } catch (final Exception e) {
                throw  new RuntimeException(e);
            }
        };

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Length", Long.toString(videoFilePath.toFile().length()));

        return ResponseEntity.ok().headers(responseHeaders).body(stream);
    }
}
