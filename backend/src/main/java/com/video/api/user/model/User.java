package com.video.api.user.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String username;
    private String password;

    @Data
    public static class UserDto {
        private UUID id;
        private String username;
    }

    @Data
    public static class AuthDto {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    public static UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
