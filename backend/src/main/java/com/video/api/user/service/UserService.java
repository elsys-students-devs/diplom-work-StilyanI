package com.video.api.user.service;

import com.video.api.user.model.User;
import com.video.api.user.model.VideoProgress;

import java.util.UUID;

public interface UserService {
    User.UserDto login(User.AuthDto authDto);
    User.UserDto register(User.AuthDto authDto);

    VideoProgress.VideoProgressDto getProgress(UUID userId, String videoId);
    VideoProgress addOrUpdateProgress(VideoProgress videoProgress);
}
