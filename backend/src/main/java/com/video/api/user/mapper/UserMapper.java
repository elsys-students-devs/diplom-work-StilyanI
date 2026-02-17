package com.video.api.user.mapper;

import com.video.api.user.dto.UserDto;
import com.video.api.user.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    UserDto toDto(User user);
}
