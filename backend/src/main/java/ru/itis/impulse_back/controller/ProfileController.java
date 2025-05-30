package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.UpdateSpecialistInfoRequest;
import ru.itis.impulse_back.dto.request.UpdateUserPhotoRequest;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.SpecialistService;
import ru.itis.impulse_back.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.uri}/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final JWTService jwtService;
    private final SpecialistService specialistService;
    private final UserService userService;

    @PostMapping("/update/specialty")
    public ResponseEntity<Void> updateSpecialties(
            @RequestBody List<Long> specialties,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token
    ) {
        Long userId = jwtService.getClaims(token.substring(7)).get("id").asLong();
        log.info("Updating specialties for user {}", userId);
        specialistService.updateSpecialties(specialties, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteAccount(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long userId = jwtService.getClaims(token.substring(7)).get("id").asLong();
        log.info("Deleting account for user {}", userId);
        userService.deleteAccount(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/info")
    public ResponseEntity<UserDetailsResponse> editProfileInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody UpdateSpecialistInfoRequest request
    ) {
        Long userId = jwtService.getClaims(token.substring(7)).get("id").asLong();
        log.info("Updating profile info for specialist {}", userId);
        UserDetailsResponse response = specialistService.updateSpecialistInfo(
                userId,
                request.getSpecialistBio(),
                request.getSpecialistPrice()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/avatar")
    public ResponseEntity<UserDetailsResponse> updateProfilePhoto(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
            @RequestBody UpdateUserPhotoRequest request
    ) {
        Long userId = jwtService.getClaims(token.substring(7)).get("id").asLong();
        log.info("Updating profile photo for user {}", userId);
        UserDetailsResponse response = userService.updateUserProfilePhoto(userId, request.getProfilePhotoUrl());
        return ResponseEntity.ok(response);
    }
}
