package com.video.api.user.mapper;

import com.video.api.user.dto.VideoProgressDto;
import com.video.api.user.model.VideoProgress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VideoProgressMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "video.id", target = "videoId")
    VideoProgressDto toDto(VideoProgress entity);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "videoId", target = "video.id")
    @Mapping(target = "id", ignore = true)
    VideoProgress toEntity(VideoProgressDto dto);
}
