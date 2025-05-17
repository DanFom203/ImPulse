package ru.itis.impulse_back.service;

import ru.itis.impulse_back.dto.request.CreateReviewRequest;
import ru.itis.impulse_back.model.Review;
import java.util.List;

public interface ReviewService {
    List<Review> getAllByClientId(Long clientId);
    void create(CreateReviewRequest request, Long clientId, Long specialistId);
}
