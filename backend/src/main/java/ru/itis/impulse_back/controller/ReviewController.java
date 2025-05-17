package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.CreateReviewRequest;
import ru.itis.impulse_back.dto.response.ReviewResponse;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.ReviewService;

import java.util.List;

@RestController
@RequestMapping("${api.uri}/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JWTService jwtService;

    @GetMapping("/by-me")
    public ResponseEntity<List<ReviewResponse>> getAllByClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {

        List<ReviewResponse> reviews = reviewService.getAllByClientId(jwtService.getClaims(token.substring(7)).get("id").asLong())
                .stream()
                .map(r -> ReviewResponse.builder()
                        .id(r.getId())
                        .client(ReviewResponse.ClientInfo.builder()
                                .id(r.getClient().getId())
                                .fullName(r.getClient().getFullName())
                                .build())
                        .specialist(ReviewResponse.SpecialistInfo.builder()
                                .id(r.getSpecialist().getId())
                                .fullName(r.getSpecialist().getFullName())
                                .rating(r.getSpecialist().getSpecialistAvgRating())
                                .specialties(r.getSpecialist().getSpecialties())
                                .build())
                        .rating(r.getRating())
                        .comment(r.getComment())
                        .createdAt(r.getCreatedAt())
                        .build()).toList();

        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/new/{specialistId}")
    public ResponseEntity<Void> create(@RequestBody CreateReviewRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long specialistId) {
        reviewService.create(request, jwtService.getClaims(token.substring(7)).get("id").asLong(), specialistId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
