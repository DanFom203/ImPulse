package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.request.CreateReviewRequest;
import ru.itis.impulse_back.dto.response.ReviewResponse;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("${api.uri}/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final JWTService jwtService;

    @GetMapping("/by-me")
    public ResponseEntity<List<ReviewResponse>> getAllByClient(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        Long clientId = jwtService.getClaims(token.substring(7)).get("id").asLong();
        log.info("Fetching reviews written by client with ID: {}", clientId);

        List<ReviewResponse> reviews = reviewService.getAllByClientId(clientId)
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

        log.debug("Found {} reviews by client ID {}", reviews.size(), clientId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/new/{specialistId}")
    public ResponseEntity<Void> create(@RequestBody CreateReviewRequest request, @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @PathVariable Long specialistId) {
        Long clientId = jwtService.getClaims(token.substring(7)).get("id").asLong();
        log.info("Client {} is creating a review for specialist {}", clientId, specialistId);

        reviewService.create(request, clientId, specialistId);

        log.info("Review created successfully by client {}", clientId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
