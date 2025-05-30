package ru.itis.impulse_back.junit.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.itis.impulse_back.dto.request.LoginRequest;
import ru.itis.impulse_back.dto.request.RegisterRequest;
import ru.itis.impulse_back.exception.PasswordsNotMatchException;
import ru.itis.impulse_back.exception.UserNotFoundException;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.UserService;
import ru.itis.impulse_back.service.impl.AuthServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private JWTService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_ShouldThrowPasswordsNotMatchException_WhenPasswordsDoNotMatch() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setFullName("Test User");
        request.setPassword("password1");
        request.setPasswordRepeat("password2");
        request.setRole("SPECIALIST");

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(PasswordsNotMatchException.class)
                .hasMessageContaining("Passwords do not match");

        verifyNoInteractions(userService, jwtService, passwordEncoder);
    }

    @Test
    void register_ShouldCreateUserAndReturnToken_WhenPasswordsMatch() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setFullName("Test User");
        request.setPassword("password");
        request.setPasswordRepeat("password");
        request.setRole("SPECIALIST");

        String encodedPassword = "encoded_password";
        String token = "jwt_token";

        when(passwordEncoder.encode(request.getPassword())).thenReturn(encodedPassword);
        doNothing().when(userService).create(any(User.class));
        when(jwtService.generateToken(any(User.class))).thenReturn(token);

        String result = authService.register(request);

        assertThat(result).isEqualTo(token);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService).create(userCaptor.capture());
        User createdUser = userCaptor.getValue();

        assertThat(createdUser.getEmail()).isEqualTo(request.getEmail());
        assertThat(createdUser.getFullName()).isEqualTo(request.getFullName());
        assertThat(createdUser.getPassword()).isEqualTo(encodedPassword);
        assertThat(createdUser.getRole()).isEqualTo(User.UserRole.SPECIALIST);
        assertThat(createdUser.getAuthority()).isEqualTo(User.UserAuthority.DEFAULT);
        assertThat(createdUser.getCreatedAt()).isNotNull();

        verify(jwtService).generateToken(createdUser);
    }

    @Test
    void login_ShouldAuthenticateAndReturnToken_WhenUserExists() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        User user = User.builder()
                .email(request.getEmail())
                .password("encoded_password")
                .build();

        String token = "jwt_token";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn(token);

        String result = authService.login(request);

        assertThat(result).isEqualTo(token);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(request.getEmail());
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_ShouldThrowUserNotFoundException_WhenUserDoesNotExist() {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@example.com");
        request.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User with this email not found");

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(request.getEmail());
        verify(jwtService, never()).generateToken(any());
    }
}
