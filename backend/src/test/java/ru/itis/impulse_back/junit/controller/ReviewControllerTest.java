package ru.itis.impulse_back.junit.controller;

import com.auth0.jwt.interfaces.Claim;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import ru.itis.impulse_back.controller.ReviewController;
import ru.itis.impulse_back.dto.request.CreateReviewRequest;
import ru.itis.impulse_back.dto.response.ReviewResponse;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.ReviewService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

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
    void getAllByClient_shouldReturnReviewList() {
        String token = "Bearer fake.jwt.token";
        Long userId = 1L;

        Claim mockClaim = mock(Claim.class);
        when(mockClaim.asLong()).thenReturn(userId);
        when(jwtService.getClaims("fake.jwt.token")).thenReturn(Map.of("id", mockClaim));

        User client = User.builder().id(userId).fullName("Client Name").build();
        User specialist = User.builder()
                .id(2L)
                .fullName("Specialist Name")
                .specialistAvgRating(4.5)
                .build();
        Review review = Review.builder()
                .id(10L)
                .client(client)
                .specialist(specialist)
                .rating(5)
                .comment("Great session")
                .build();

        when(reviewService.getAllByClientId(userId)).thenReturn(List.of(review));

        ResponseEntity<List<ReviewResponse>> response = reviewController.getAllByClient(token);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Client Name", response.getBody().get(0).getClient().getFullName());
        assertEquals("Specialist Name", response.getBody().get(0).getSpecialist().getFullName());
    }

    @Test
    void create_shouldCallServiceAndReturn201() {
        String token = "Bearer fake.jwt.token";
        Long clientId = 1L;
        Long specialistId = 2L;
        CreateReviewRequest request = new CreateReviewRequest("Отлично", 5);

        Claim mockClaim = mock(Claim.class);
        when(mockClaim.asLong()).thenReturn(clientId);
        when(jwtService.getClaims("fake.jwt.token")).thenReturn(Map.of("id", mockClaim));

        ResponseEntity<Void> response = reviewController.create(request, token, specialistId);

        verify(reviewService, times(1)).create(request, clientId, specialistId);
        assertEquals(201, response.getStatusCodeValue());
    }
}
