package com.video.api.user.service.impl;

import com.video.api.user.dto.UserDto;
import com.video.api.user.exception.LoginFailedException;
import com.video.api.user.mapper.UserMapper;
import com.video.api.user.model.User;
import com.video.api.user.repository.UserRepository;
import com.video.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto getUserById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new LoginFailedException("User with id [" + id + "] not found"));

        return userMapper.toDto(user);
    }
}
