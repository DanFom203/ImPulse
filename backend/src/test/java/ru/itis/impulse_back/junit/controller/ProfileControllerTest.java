package ru.itis.impulse_back.junit.controller;


import com.auth0.jwt.interfaces.Claim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.itis.impulse_back.controller.ProfileController;
import ru.itis.impulse_back.dto.request.UpdateSpecialistInfoRequest;
import ru.itis.impulse_back.dto.request.UpdateUserPhotoRequest;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.SpecialistService;
import ru.itis.impulse_back.service.UserService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileControllerTest {

    private JWTService jwtService;
    private SpecialistService specialistService;
    private UserService userService;
    private ProfileController profileController;

    @BeforeEach
    void setUp() {
        jwtService = mock(JWTService.class);
        specialistService = mock(SpecialistService.class);
        userService = mock(UserService.class);
        profileController = new ProfileController(jwtService, specialistService, userService);
    }

    @Test
    void updateSpecialties_shouldCallService() {
        String token = "Bearer token";
        Long userId = 1L;
        List<Long> specialties = List.of(1L, 2L);

        Claim claim = mock(Claim.class);
        when(claim.asLong()).thenReturn(userId);
        when(jwtService.getClaims("token")).thenReturn(Map.of("id", claim));

        ResponseEntity<Void> response = profileController.updateSpecialties(specialties, token);

        verify(specialistService).updateSpecialties(specialties, userId);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteAccount_shouldCallService() {
        String token = "Bearer token";
        Long userId = 2L;

        Claim claim = mock(Claim.class);
        when(claim.asLong()).thenReturn(userId);
        when(jwtService.getClaims("token")).thenReturn(Map.of("id", claim));

        ResponseEntity<Void> response = profileController.deleteAccount(token);

        verify(userService).deleteAccount(userId);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void editProfileInfo_shouldReturnUpdatedUserDetails() {
        String token = "Bearer token";
        Long userId = 3L;

        UpdateSpecialistInfoRequest request = new UpdateSpecialistInfoRequest("Bio", 3000);

        UserDetailsResponse mockResponse = UserDetailsResponse.builder()
                .fullName("Test User")
                .specialistBio("Bio")
                .specialistAppointmentPrice(3000)
                .build();

        Claim claim = mock(Claim.class);
        when(claim.asLong()).thenReturn(userId);
        when(jwtService.getClaims("token")).thenReturn(Map.of("id", claim));
        when(specialistService.updateSpecialistInfo(userId, "Bio", 3000)).thenReturn(mockResponse);

        ResponseEntity<UserDetailsResponse> response = profileController.editProfileInfo(token, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test User", response.getBody().getFullName());
        assertEquals(3000, response.getBody().getSpecialistAppointmentPrice());
    }

    @Test
    void updateProfilePhoto_shouldReturnUpdatedUserDetails() {
        String token = "Bearer token";
        Long userId = 4L;

        UpdateUserPhotoRequest request = new UpdateUserPhotoRequest("https://photo.url");

        UserDetailsResponse mockResponse = UserDetailsResponse.builder()
                .profileImageUrl("https://photo.url")
                .build();

        Claim claim = mock(Claim.class);
        when(claim.asLong()).thenReturn(userId);
        when(jwtService.getClaims("token")).thenReturn(Map.of("id", claim));
        when(userService.updateUserProfilePhoto(userId, "https://photo.url")).thenReturn(mockResponse);

        ResponseEntity<UserDetailsResponse> response = profileController.updateProfilePhoto(token, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("https://photo.url", response.getBody().getProfileImageUrl());
    }
}

