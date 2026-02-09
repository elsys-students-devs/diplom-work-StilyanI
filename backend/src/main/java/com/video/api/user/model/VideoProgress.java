package com.video.api.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class VideoProgress {
    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    private UUID userId;

    @NotBlank
    private String videoId;

    @Min(0)
    private Float progressSeconds;

    @Min(0)
    @Max(100)
    private Integer progressPercent;

    @Data
    public static class VideoProgressDto{
        private UUID userId;
        private String videoId;
        private Float progressSeconds;
        private Integer progressPercent;
    }

    public static VideoProgressDto mapToDto(VideoProgress videoProgress){
        VideoProgressDto videoProgressDto = new VideoProgressDto();
        videoProgressDto.setUserId(videoProgress.getUserId());
        videoProgressDto.setVideoId(videoProgress.getVideoId());
        videoProgressDto.setProgressSeconds(videoProgress.getProgressSeconds());
        videoProgressDto.setProgressPercent(videoProgress.getProgressPercent());
        return videoProgressDto;
    }
}
