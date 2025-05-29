package ru.itis.impulse_back.junit.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.ReviewRepository;
import ru.itis.impulse_back.repository.UserRepository;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void testFindAllByClientId() {
        // given
        User client = userRepository.save(User.builder().email("client@test.com").build());
        User specialist = userRepository.save(User.builder().email("specialist@test.com").build());

        Review review = Review.builder()
                .client(client)
                .specialist(specialist)
                .rating(5)
                .comment("Отлично!")
                .createdAt(new Date())
                .build();

        reviewRepository.save(review);

        // when
        List<Review> found = reviewRepository.findAllByClientId(client.getId());

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getComment()).isEqualTo("Отлично!");
    }

    @Test
    @Transactional
    void testDeleteAllByClientOrSpecialist() {
        // given
        User client = userRepository.save(User.builder().email("client2@test.com").build());
        User specialist = userRepository.save(User.builder().email("specialist2@test.com").build());

        Review review1 = Review.builder()
                .client(client)
                .specialist(specialist)
                .comment("Удалить 1")
                .rating(4)
                .createdAt(new Date())
                .build();

        Review review2 = Review.builder()
                .client(client)
                .comment("Удалить 2")
                .rating(5)
                .createdAt(new Date())
                .build();

        Review review3 = Review.builder()
                .specialist(specialist)
                .comment("Удалить 3")
                .rating(3)
                .createdAt(new Date())
                .build();

        Review review4 = Review.builder()
                .comment("Оставить")
                .rating(5)
                .createdAt(new Date())
                .build();

        reviewRepository.saveAll(List.of(review1, review2, review3, review4));

        // when
        reviewRepository.deleteAllByClientOrSpecialist(client, specialist);

        // then
        List<Review> remaining = reviewRepository.findAll();
        assertThat(remaining).hasSize(1);
        assertThat(remaining.get(0).getComment()).isEqualTo("Оставить");
    }
}
