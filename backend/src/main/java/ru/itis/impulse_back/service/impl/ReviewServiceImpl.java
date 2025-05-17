package ru.itis.impulse_back.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.dto.request.CreateReviewRequest;
import ru.itis.impulse_back.exception.SpecialistNotFoundException;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.ReviewRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.ReviewService;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    public List<Review> getAllByClientId(Long clientId) {
        return reviewRepository.findAllByClientId(clientId);
    }

    @Override
    public void create(CreateReviewRequest request, Long clientId, Long specialistId) {
        Optional<User> specialistOptional = userRepository.findById(specialistId);

        if (specialistOptional.isEmpty()) {
            throw new SpecialistNotFoundException("Specialist with id " + specialistId + " not found");
        }

        User specialist = specialistOptional.get();

        Double avgRating = specialist.getSpecialistAvgRating();

        if (avgRating == null) {
            specialist.setSpecialistAvgRating(Double.valueOf(request.getRating()));
        } else {
            Integer sumRating = specialist.getSpecialistReviews().stream().mapToInt(Review::getRating).sum();
            Double newRating = (double) (sumRating + request.getRating()) / (specialist.getSpecialistReviews().size() + 1);
            specialist.setSpecialistAvgRating(Double.parseDouble(new DecimalFormat("0.00").format(newRating)));
        }

        Review review = Review.builder()
                .comment(request.getComment())
                .rating(request.getRating())
                .createdAt(new Date())
                .client(userRepository.findById(clientId).get())
                .specialist(specialist)
                .build();

        reviewRepository.save(review);
        userRepository.save(specialist);
    }
}
