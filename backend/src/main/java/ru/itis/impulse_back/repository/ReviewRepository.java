package ru.itis.impulse_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impulse_back.model.Review;
import ru.itis.impulse_back.model.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByClientId(Long clientId);
    void deleteAllByClientOrSpecialist(User client, User specialist);
}
