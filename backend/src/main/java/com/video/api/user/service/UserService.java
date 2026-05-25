package com.video.api.user.service;

import com.video.api.user.dto.UserDto;

import java.util.UUID;

public interface UserService {
    UserDto getUserById(UUID id);
}
