package com.video.api.user.service;

import com.video.api.user.exception.LoginFailedException;
import com.video.api.user.exception.UserExistsException;
import com.video.api.user.model.User;
import com.video.api.user.model.VideoProgress;
import com.video.api.user.repository.UserRepository;
import com.video.api.user.repository.VideoProgressRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VideoProgressRepository videoProgressRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, VideoProgressRepository videoProgressRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.videoProgressRepository = videoProgressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User.UserDto login(User.AuthDto authDto) {
        User user = userRepository.findUserByUsername(authDto.getUsername())
                .orElseThrow(() -> new LoginFailedException("Account with this username was not found"));

        if (!passwordEncoder.matches(authDto.getPassword(), user.getPassword())) {
            throw new LoginFailedException("Incorrect password");
        }

        return User.mapToDto(user);
    }

    @Override
    public User.UserDto register(User.AuthDto authDto) {
        userRepository.findUserByUsername(authDto.getUsername())
                .ifPresent(_ -> {
                    throw new UserExistsException("Account with username [" + authDto.getUsername() + "] already exists");
                });

        User user = new User();
        user.setUsername(authDto.getUsername());
        user.setPassword(passwordEncoder.encode(authDto.getPassword()));

        return User.mapToDto(userRepository.save(user));
    }

    @Override
    public VideoProgress.VideoProgressDto getProgress(UUID userId, String videoId) {
        VideoProgress vp = videoProgressRepository
                .findVideoProgressByUserIdAndVideoId(userId, videoId)
                .orElseThrow();

        return VideoProgress.mapToDto(vp);
    }

    @Override
    public VideoProgress addOrUpdateProgress(VideoProgress videoProgress) {
        VideoProgress vp = videoProgressRepository
                .findVideoProgressByUserIdAndVideoId(videoProgress.getUserId(), videoProgress.getVideoId())
                .orElse(null);
        if (vp != null) {
            vp.setProgressPercent(videoProgress.getProgressPercent());
            vp.setProgressSeconds(videoProgress.getProgressSeconds());
            return videoProgressRepository.save(vp);
        }  else {
            return videoProgressRepository.save(videoProgress);
        }
    }
}
