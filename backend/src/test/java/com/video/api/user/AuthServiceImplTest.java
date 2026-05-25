package com.video.api.user;

import com.video.api.user.dto.AuthDto;
import com.video.api.user.dto.UserDto;
import com.video.api.user.exception.LoginFailedException;
import com.video.api.user.exception.UserExistsException;
import com.video.api.user.mapper.UserMapper;
import com.video.api.user.model.User;
import com.video.api.user.repository.UserRepository;
import com.video.api.user.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void loginReturnsMappedUserWhenCredentialsAreValid() {
        AuthDto authDto = authDto("testUsername", "password");
        User user = user(UUID.randomUUID(), "testUsername", "encoded");
        UserDto mappedUser = userDto(user.getId(), "testUsername");

        when(userRepository.findUserByUsername("testUsername")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);
        when(userMapper.toDto(user)).thenReturn(mappedUser);

        UserDto result = authService.login(authDto);

        assertThat(result).isSameAs(mappedUser);
        verify(userMapper).toDto(user);
    }

    @Test
    void loginThrowsWhenUserDoesNotExist() {
        AuthDto authDto = authDto("missing", "password");
        when(userRepository.findUserByUsername("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(authDto))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("Account with this username was not found");
    }

    @Test
    void loginThrowsWhenPasswordDoesNotMatch() {
        AuthDto authDto = authDto("testUsername", "wrong");
        User user = user(UUID.randomUUID(), "testUsername", "encoded");

        when(userRepository.findUserByUsername("testUsername")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(authDto))
                .isInstanceOf(LoginFailedException.class)
                .hasMessage("Incorrect password");

        verify(userMapper, never()).toDto(any());
    }

    @Test
    void registerPersistsUserWithEncodedPassword() {
        AuthDto authDto = authDto("newUser", "plain");
        User savedUser = user(UUID.randomUUID(), "newUser", "hashed");
        UserDto mappedUser = userDto(savedUser.getId(), "newUser");

        when(userRepository.findUserByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(mappedUser);

        UserDto result = authService.register(authDto);

        assertThat(result).isSameAs(mappedUser);
        verify(passwordEncoder).encode("plain");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerThrowsWhenUsernameAlreadyExists() {
        AuthDto authDto = authDto("existing", "plain");
        when(userRepository.findUserByUsername("existing")).thenReturn(Optional.of(user(UUID.randomUUID(), "existing", "encoded")));

        assertThatThrownBy(() -> authService.register(authDto))
                .isInstanceOf(UserExistsException.class)
                .hasMessage("Account with username [existing] already exists");

        verify(userRepository, never()).save(any());
    }

    private static AuthDto authDto(String username, String password) {
        AuthDto dto = new AuthDto();
        dto.setUsername(username);
        dto.setPassword(password);
        return dto;
    }

    private static User user(UUID id, String username, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    private static UserDto userDto(UUID id, String username) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setUsername(username);
        return dto;
    }
}
