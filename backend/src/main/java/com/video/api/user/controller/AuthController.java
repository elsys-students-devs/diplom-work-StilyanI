package com.video.api.user.controller;

import com.video.api.user.dto.AuthDto;
import com.video.api.user.dto.UserDto;
import com.video.api.user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody AuthDto authDto) {
        return ResponseEntity.ok(authService.login(authDto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody AuthDto authDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(authDto));
    }
}
