package com.video.api.user.service.impl;

import com.video.api.user.dto.VideoProgressDto;
import com.video.api.user.exception.UserNotExistsException;
import com.video.api.user.mapper.VideoProgressMapper;
import com.video.api.user.model.User;
import com.video.api.user.model.VideoProgress;
import com.video.api.user.repository.UserRepository;
import com.video.api.user.repository.VideoProgressRepository;
import com.video.api.user.service.VideoProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoProgressServiceImpl implements VideoProgressService {
    private final VideoProgressRepository videoProgressRepository;
    private final VideoProgressMapper videoProgressMapper;
    private final UserRepository userRepository;

    @Override
    public VideoProgressDto getProgress(UUID userId, Long videoId) {
        VideoProgress vp = videoProgressRepository
                .findVideoProgressByUserIdAndVideoId(userId, videoId)
                .orElseThrow();

        return videoProgressMapper.toDto(vp);
    }

    @Override
    public List<Long> getProgressForUser(UUID userId) {
        List<VideoProgress> videoProgressList = videoProgressRepository.findVideoProgressByUserId(userId);
        List<Long> videoIdList = new ArrayList<>();

        for (VideoProgress vp : videoProgressList) {
            videoIdList.add(vp.getVideo().getId());
        }

        return videoIdList;
    }

    @Override
    public VideoProgressDto saveProgress(VideoProgressDto videoProgressDto) {
        VideoProgress vp = videoProgressRepository
                .findVideoProgressByUserIdAndVideoId(videoProgressDto.getUserId(), videoProgressDto.getVideoId())
                .orElse(null);
        if (vp != null) {
            vp.setProgressPercent(videoProgressDto.getProgressPercent());
            vp.setProgressSeconds(videoProgressDto.getProgressSeconds());

            return videoProgressMapper.toDto(videoProgressRepository.save(vp));
        }  else {
            vp = videoProgressMapper.toEntity(videoProgressDto);

            User user = userRepository.findById(videoProgressDto.getUserId()).orElseThrow(() -> new UserNotExistsException("Error creating video progress: Could not find user with id[" + videoProgressDto.getUserId() + "]"));
            vp.setUser(user);

            return videoProgressMapper.toDto(videoProgressRepository.save(vp));
        }
    }
}
