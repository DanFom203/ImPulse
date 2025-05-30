package ru.itis.impulse_back.junit.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.Message;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.ChatRepository;
import ru.itis.impulse_back.repository.MessageRepository;
import ru.itis.impulse_back.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Test
    @DisplayName("Should save and retrieve messages by chat ordered by createdAt")
    void testFindAllByChatOrderByCreatedAtAsc() {
        User sender = userRepository.save(User.builder().email("sender@test.com").build());
        User receiver = userRepository.save(User.builder().email("receiver@test.com").build());
        Chat chat = chatRepository.save(Chat.builder().firstParticipant(sender).secondParticipant(receiver).build());

        Message msg1 = Message.builder()
                .chat(chat)
                .sender(sender)
                .receiver(receiver)
                .body("First")
                .createdAt(new Date(System.currentTimeMillis() - 10000))
                .build();

        Message msg2 = Message.builder()
                .chat(chat)
                .sender(receiver)
                .receiver(sender)
                .body("Second")
                .createdAt(new Date())
                .build();

        messageRepository.save(msg1);
        messageRepository.save(msg2);

        List<Message> messages = messageRepository.findAllByChatOrderByCreatedAtAsc(chat);

        assertThat(messages).hasSize(2);
        assertThat(messages.get(0).getBody()).isEqualTo("First");
        assertThat(messages.get(1).getBody()).isEqualTo("Second");
    }

    @Test
    @DisplayName("Should find top (latest) message by chat")
    void testFindTopByChatOrderByCreatedAtDesc() {
        User sender = userRepository.save(User.builder().email("sender2@test.com").build());
        User receiver = userRepository.save(User.builder().email("receiver2@test.com").build());
        Chat chat = chatRepository.save(Chat.builder().firstParticipant(sender).secondParticipant(receiver).build());


        Optional<Message> topMessage = messageRepository.findTopByChatOrderByCreatedAtDesc(chat);

        assertThat(topMessage).isPresent();
        assertThat(topMessage.get().getBody()).isEqualTo("New");
    }

    @Test
    @DisplayName("Should delete all messages by sender or receiver")
    void testDeleteAllBySenderOrReceiver() {
        User user1 = userRepository.save(User.builder().email("user1@test.com").build());
        User user2 = userRepository.save(User.builder().email("user2@test.com").build());
        Chat chat = chatRepository.save(Chat.builder().firstParticipant(user1).secondParticipant(user2).build());

        messageRepository.save(Message.builder()
                .chat(chat)
                .sender(user1)
                .receiver(user2)
                .body("Msg 1")
                .createdAt(new Date())
                .build());

        messageRepository.save(Message.builder()
                .chat(chat)
                .sender(user2)
                .receiver(user1)
                .body("Msg 2")
                .createdAt(new Date())
                .build());

        messageRepository.deleteAllBySenderOrReceiver(user1, user1);

        List<Message> remaining = messageRepository.findAll();
        assertThat(remaining).isEmpty();
    }
}
