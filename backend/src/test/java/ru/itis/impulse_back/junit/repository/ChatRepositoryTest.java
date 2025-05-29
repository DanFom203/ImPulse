package ru.itis.impulse_back.junit.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.ChatRepository;
import ru.itis.impulse_back.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ChatRepositoryTest {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    void testFindByFirstParticipantIdAndSecondParticipantId() {
        // given
        User user1 = userRepository.save(User.builder().email("user1@test.com").build());
        User user2 = userRepository.save(User.builder().email("user2@test.com").build());

        Chat chat = Chat.builder()
                .firstParticipant(user1)
                .secondParticipant(user2)
                .build();

        chatRepository.save(chat);

        // when
        Optional<Chat> found = chatRepository.findByFirstParticipantIdAndSecondParticipantId(user1.getId(), user2.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getFirstParticipant().getEmail()).isEqualTo("user1@test.com");
        assertThat(found.get().getSecondParticipant().getEmail()).isEqualTo("user2@test.com");
    }

    @Test
    @Transactional
    void testDeleteAllByFirstParticipantOrSecondParticipant() {
        // given
        User user1 = userRepository.save(User.builder().email("u1@test.com").build());
        User user2 = userRepository.save(User.builder().email("u2@test.com").build());
        User user3 = userRepository.save(User.builder().email("u3@test.com").build());

        Chat chat1 = Chat.builder().firstParticipant(user1).secondParticipant(user2).build(); // будет удалён
        Chat chat2 = Chat.builder().firstParticipant(user2).secondParticipant(user3).build(); // НЕ будет удалён
        Chat chat3 = Chat.builder().firstParticipant(user3).secondParticipant(user1).build(); // НЕ будет удалён
        Chat chat4 = Chat.builder().firstParticipant(user3).secondParticipant(user3).build(); // НЕ будет удалён

        chatRepository.saveAll(List.of(chat1, chat2, chat3, chat4));

        // when
        chatRepository.deleteAllByFirstParticipantOrSecondParticipant(user1, user2);

        // then
        List<Chat> remainingChats = chatRepository.findAll();

        // chat1 будет удалён (user1 — firstParticipant)
        assertThat(remainingChats).hasSize(3);
        assertThat(remainingChats).noneMatch(chat ->
                (chat.getFirstParticipant().equals(user1) || chat.getSecondParticipant().equals(user2))
        );
    }
}
