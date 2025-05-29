package ru.itis.impulse_back.junit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.impulse_back.dto.request.CreateReviewRequest;
import ru.itis.impulse_back.exception.SpecialistNotFoundException;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.ReviewRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.impl.ReviewServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    void getAllByClientId_ShouldReturnReviews() {
        Long clientId = 1L;
        List<Review> reviews = List.of(new Review(), new Review());

        when(reviewRepository.findAllByClientId(clientId)).thenReturn(reviews);

        List<Review> result = reviewService.getAllByClientId(clientId);

        assertThat(result).isEqualTo(reviews);
        verify(reviewRepository).findAllByClientId(clientId);
    }

    @Test
    void create_ShouldThrow_WhenSpecialistNotFound() {
        Long specialistId = 2L;
        Long clientId = 1L;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(5);
        request.setComment("Good");

        when(userRepository.findById(specialistId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.create(request, clientId, specialistId))
                .isInstanceOf(SpecialistNotFoundException.class)
                .hasMessageContaining("Specialist with id " + specialistId + " not found");
    }

    @Test
    void create_ShouldSaveReviewAndUpdateAvgRating_WhenNoPreviousRating() {
        Long specialistId = 2L;
        Long clientId = 1L;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(4);
        request.setComment("Nice work");

        User specialist = new User();
        specialist.setSpecialistAvgRating(null);
        specialist.setSpecialistReviews(new ArrayList<>());

        User client = new User();

        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));

        reviewService.create(request, clientId, specialistId);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review savedReview = reviewCaptor.getValue();
        assertThat(savedReview.getComment()).isEqualTo("Nice work");
        assertThat(savedReview.getRating()).isEqualTo(4);
        assertThat(savedReview.getSpecialist()).isEqualTo(specialist);
        assertThat(savedReview.getClient()).isEqualTo(client);
        assertThat(savedReview.getCreatedAt()).isNotNull();

        // Avg rating updated to the rating of the new review
        assertThat(specialist.getSpecialistAvgRating()).isEqualTo(4.0);
        verify(userRepository).save(specialist);
    }

    @Test
    void create_ShouldSaveReviewAndUpdateAvgRating_WhenPreviousRatingsExist() {
        Long specialistId = 2L;
        Long clientId = 1L;
        CreateReviewRequest request = new CreateReviewRequest();
        request.setRating(5);
        request.setComment("Excellent");

        User specialist = new User();
        specialist.setSpecialistAvgRating(4.0);

        Review prevReview1 = new Review();
        prevReview1.setRating(3);
        Review prevReview2 = new Review();
        prevReview2.setRating(5);

        List<Review> reviews = new ArrayList<>(List.of(prevReview1, prevReview2));
        specialist.setSpecialistReviews(reviews);

        User client = new User();

        when(userRepository.findById(specialistId)).thenReturn(Optional.of(specialist));
        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));

        reviewService.create(request, clientId, specialistId);

        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review savedReview = reviewCaptor.getValue();
        assertThat(savedReview.getComment()).isEqualTo("Excellent");
        assertThat(savedReview.getRating()).isEqualTo(5);
        assertThat(savedReview.getSpecialist()).isEqualTo(specialist);
        assertThat(savedReview.getClient()).isEqualTo(client);
        assertThat(savedReview.getCreatedAt()).isNotNull();

        // Средний рейтинг: (3 + 5 + 5) / 3 = 4.33 (округлён)
        assertThat(specialist.getSpecialistAvgRating()).isEqualTo(4.33);
        verify(userRepository).save(specialist);
    }
}