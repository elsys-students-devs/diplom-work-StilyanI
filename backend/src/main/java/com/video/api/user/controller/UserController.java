package com.video.api.user.controller;

import com.video.api.user.model.User;
import com.video.api.user.model.VideoProgress;
import com.video.api.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<User.UserDto> login(@Valid @RequestBody User.AuthDto authDto) {
        return ResponseEntity.ok(userService.login(authDto));
    }

    @PostMapping("/register")
    public ResponseEntity<User.UserDto> register(@Valid @RequestBody User.AuthDto authDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(authDto));
    }

    @GetMapping("/video-progress")
    public ResponseEntity<VideoProgress.VideoProgressDto> getVideoProgress(@RequestParam UUID userId, @RequestParam String videoId) {
        return ResponseEntity.ok(userService.getProgress(userId, videoId));
    }

    @PutMapping("/video-progress")
    public ResponseEntity<VideoProgress> addVideoProgress(@Valid @RequestBody VideoProgress videoProgress) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addOrUpdateProgress(videoProgress));
    }
}
