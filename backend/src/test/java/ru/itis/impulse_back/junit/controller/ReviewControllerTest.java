package ru.itis.impulse_back.junit.controller;

import com.auth0.jwt.interfaces.Claim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.itis.impulse_back.controller.ReviewController;
import ru.itis.impulse_back.dto.request.CreateReviewRequest;
import ru.itis.impulse_back.dto.response.ReviewResponse;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.ReviewService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ReviewControllerTest {

    private ReviewService reviewService;
    private JWTService jwtService;
    private ReviewController reviewController;

    @BeforeEach
    void setUp() {
        reviewService = mock(ReviewService.class);
        jwtService = mock(JWTService.class);
        reviewController = new ReviewController(reviewService, jwtService);
    }

    @Test
    void getAllByClient_shouldReturnListOfReviews() {
        String token = "Bearer faketoken";
        Long clientId = 1L;

        Map<String, Claim> claims = new HashMap<>();
        Claim idClaim = mock(Claim.class);
        when(idClaim.asLong()).thenReturn(clientId);
        claims.put("id", idClaim);
        when(jwtService.getClaims("faketoken")).thenReturn(claims);

        User client = new User();
        client.setId(clientId);
        client.setFullName("Client User");

        User specialist = new User();
        specialist.setId(2L);
        specialist.setFullName("Specialist Name");
        specialist.setSpecialistAvgRating(4.5);


        Review review = new Review();
        review.setId(10L);
        review.setClient(client);
        review.setSpecialist(specialist);
        review.setRating(5);
        review.setComment("Great session");

        when(reviewService.getAllByClientId(clientId)).thenReturn(List.of(review));

        ResponseEntity<List<ReviewResponse>> response = reviewController.getAllByClient(token);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Client User", response.getBody().get(0).getClient().getFullName());
        assertEquals("Specialist Name", response.getBody().get(0).getSpecialist().getFullName());
    }

    @Test
    void create_shouldReturnCreatedStatus() {
        String token = "Bearer faketoken";
        Long clientId = 1L;
        Long specialistId = 2L;

        CreateReviewRequest request = CreateReviewRequest.builder()
                .rating(5)
                .comment("Excellent!")
                .build();

        Map<String, Claim> claims = new HashMap<>();
        Claim idClaim = mock(Claim.class);
        when(idClaim.asLong()).thenReturn(clientId);
        claims.put("id", idClaim);
        when(jwtService.getClaims("faketoken")).thenReturn(claims);

        ResponseEntity<Void> response = reviewController.create(request, token, specialistId);

        verify(reviewService).create(request, clientId, specialistId);

        assertEquals(201, response.getStatusCodeValue());
    }
}
