package com.video.api.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class VideoProgressDto{
    @NotNull
    private UUID userId;

    @NotBlank
    private String videoId;

    @Min(0)
    private Long progressSeconds;

    @Min(0)
    @Max(100)
    private Integer progressPercent;
}
