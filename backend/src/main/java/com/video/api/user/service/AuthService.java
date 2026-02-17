package com.video.api.user.service;

import com.video.api.user.dto.AuthDto;
import com.video.api.user.dto.UserDto;

public interface AuthService {
    UserDto login(AuthDto authDto);
    UserDto register(AuthDto authDto);
}
