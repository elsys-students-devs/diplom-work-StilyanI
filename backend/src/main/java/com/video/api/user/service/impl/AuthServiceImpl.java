package com.video.api.user.service.impl;

import com.video.api.user.dto.AuthDto;
import com.video.api.user.dto.UserDto;
import com.video.api.user.exception.LoginFailedException;
import com.video.api.user.exception.UserExistsException;
import com.video.api.user.mapper.UserMapper;
import com.video.api.user.model.User;
import com.video.api.user.repository.UserRepository;
import com.video.api.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDto login(AuthDto authDto) {
        User user = userRepository.findUserByUsername(authDto.getUsername())
                .orElseThrow(() -> new LoginFailedException("Account with this username was not found"));

        if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Incorrect password");
        }

        return userMapper.toDto(user);
    }

    @Override
    public UserDto register(AuthDto authDto) {
        userRepository.findUserByUsername(authDto.getUsername())
                .ifPresent(_ -> {
                    throw new UserExistsException("Account with username [" + authDto.getUsername() + "] already exists");
                });

        User user = new User();
        user.setUsername(authDto.getUsername());
        user.setPassword(passwordEncoder.encode(authDto.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }
}
