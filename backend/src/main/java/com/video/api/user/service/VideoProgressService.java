package com.video.api.user.service;

import com.video.api.user.dto.VideoProgressDto;

import java.util.List;
import java.util.UUID;

public interface VideoProgressService {
    VideoProgressDto getProgress(UUID userId, Long videoId);
    List<Long> getProgressForUser(UUID userId);
    VideoProgressDto saveProgress(VideoProgressDto videoProgressDto);
}
