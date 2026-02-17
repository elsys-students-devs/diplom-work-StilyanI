package com.video.api.user.service;

import com.video.api.user.dto.VideoProgressDto;

import java.util.UUID;

public interface VideoProgressService {
    VideoProgressDto getProgress(UUID userId, String videoId);
    VideoProgressDto saveProgress(VideoProgressDto videoProgressDto);
}
