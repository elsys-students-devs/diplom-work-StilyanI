package com.video.api.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class AuthDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
