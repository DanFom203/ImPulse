package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itis.impulse_back.dto.UserDto;
import ru.itis.impulse_back.dto.request.LoginRequest;
import ru.itis.impulse_back.dto.request.RegisterRequest;
import ru.itis.impulse_back.dto.response.LoginResponse;
import ru.itis.impulse_back.dto.response.RegisterResponse;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.service.AuthService;
import ru.itis.impulse_back.service.UserService;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@RestController
@RequestMapping("${api.uri}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        String token = authService.register(registerRequest);
        UserDetailsResponse userDetailsResponse = UserDetailsResponse.builder()
                .email(registerRequest.getEmail())
                .fullName(registerRequest.getFullName())
                .role(registerRequest.getRole())
                .authority(String.valueOf(User.UserAuthority.DEFAULT))
                .build();

        UserDto user = userService.getByEmail(registerRequest.getEmail());
        
        if (user.getRole().equals(User.UserRole.SPECIALIST)) {
                userDetailsResponse.setSpecialistBio(user.getSpecialistBio());

                if (user.getSpecialistReviews() != null && !user.getSpecialistReviews().isEmpty()) {
                        Double rating = (double) user.getSpecialistReviews().stream()
                                        .map(Review::getRating)
                                        .reduce(0, Integer::sum) / user.getSpecialistReviews().size();

                        userDetailsResponse.setSpecialistRating(
                                        Double.parseDouble(new DecimalFormat("0.00").format(rating)));
                }

                userDetailsResponse.setSpecialistRating(user.getSpecialistAvgRating());
                userDetailsResponse.setSpecialistAppointmentPrice(user.getSpecialistAppointmentPrice());
                userDetailsResponse.setSpecialties(user.getSpecialties());
        }        

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RegisterResponse.builder()
                        .token(token)
                        .details(userDetailsResponse)
                        .build());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);

        UserDto user = userService.getByEmail(loginRequest.getEmail());

        UserDetailsResponse userDetailsResponse = UserDetailsResponse.builder()
                .email(loginRequest.getEmail())
                .fullName(user.getFullName())
                .role(String.valueOf(user.getRole()))
                .authority(String.valueOf(user.getAuthority()))
                .createdAt(user.getCreatedAt())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        if (user.getRole().equals(User.UserRole.SPECIALIST)) {
            userDetailsResponse.setSpecialistBio(user.getSpecialistBio());

            if (user.getSpecialistReviews() != null && !user.getSpecialistReviews().isEmpty()) {
                Double rating =  (double) user.getSpecialistReviews().stream()
                        .map(Review::getRating)
                        .reduce(0, Integer::sum) / user.getSpecialistReviews().size();

                DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
                DecimalFormat df = new DecimalFormat("0.00", symbols);
                userDetailsResponse.setSpecialistRating(Double.parseDouble(df.format(rating)));
            }

            userDetailsResponse.setSpecialistRating(user.getSpecialistAvgRating());
            userDetailsResponse.setSpecialistAppointmentPrice(user.getSpecialistAppointmentPrice());
            userDetailsResponse.setSpecialties(user.getSpecialties());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(LoginResponse.builder()
                        .token(token)
                        .details(userDetailsResponse)
                        .build());
    }
}
