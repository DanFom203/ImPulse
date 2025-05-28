package ru.itis.impulse_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.Message;
import ru.itis.impulse_back.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByChatOrderByCreatedAtAsc(Chat chat);
    Optional<Message> findTopByChatOrderByCreatedAtDesc(Chat chat);
    void deleteAllBySenderOrReceiver(User sender, User receiver);
}
