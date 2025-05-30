package ru.itis.impulse_back.junit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.itis.impulse_back.controller.AuthController;
import ru.itis.impulse_back.dto.UserDto;
import ru.itis.impulse_back.dto.request.LoginRequest;
import ru.itis.impulse_back.dto.request.RegisterRequest;
import ru.itis.impulse_back.dto.response.LoginResponse;
import ru.itis.impulse_back.dto.response.RegisterResponse;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.service.AuthService;
import ru.itis.impulse_back.service.UserService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    private AuthService authService;
    private UserService userService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        userService = mock(UserService.class);
        authController = new AuthController(authService, userService);
    }

    @Test
    void register_shouldReturnCreatedResponse() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password123")
                .fullName("Test User")
                .role(String.valueOf(User.UserRole.CLIENT))
                .build();

        UserDto mockUser = UserDto.builder()
                .email("test@example.com")
                .fullName("Test User")
                .role(User.UserRole.CLIENT)
                .authority(User.UserAuthority.DEFAULT)
                .build();

        when(authService.register(request)).thenReturn("fake-token");
        when(userService.getByEmail("test@example.com")).thenReturn(mockUser);

        ResponseEntity<RegisterResponse> response = authController.register(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("fake-token", response.getBody().getToken());
        assertEquals("test@example.com", response.getBody().getDetails().getEmail());
        assertEquals("Test User", response.getBody().getDetails().getFullName());
        assertEquals("CLIENT", response.getBody().getDetails().getRole());
        assertEquals("DEFAULT", response.getBody().getDetails().getAuthority());
    }
    @Test
    void login_shouldReturnOkResponse() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        UserDto mockUser = UserDto.builder()
                .email("test@example.com")
                .fullName("Test User")
                .role(User.UserRole.CLIENT)
                .authority(User.UserAuthority.DEFAULT)
                .profileImageUrl("http://image.url")
                .specialistReviews(Collections.emptyList()) //
                .build();

        when(authService.login(request)).thenReturn("login-token");
        when(userService.getByEmail("test@example.com")).thenReturn(mockUser);

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("login-token", response.getBody().getToken());
        assertEquals("test@example.com", response.getBody().getDetails().getEmail());
        assertEquals("Test User", response.getBody().getDetails().getFullName());
        assertEquals("CLIENT", response.getBody().getDetails().getRole());
        assertEquals("DEFAULT", response.getBody().getDetails().getAuthority());
    }

}
