package ru.itis.impulse_back.junit.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User createAndSaveUser(String email) {
        User user = new User();
        user.setEmail(email);
        user.setFullName("Test User");
        user.setPassword("password");
        // заполняем другие обязательные поля, если есть:
        user.setRole(User.UserRole.CLIENT);
        user.setAuthority(User.UserAuthority.DEFAULT);
        // createdAt и profileImageUrl могут быть null или установить:
        user.setCreatedAt(null);
        user.setProfileImageUrl(null);
        return userRepository.save(user);
    }

    @Test
    @DisplayName("findByEmail() возвращает пользователя, если он есть")
    void findByEmail_existingEmail_returnsUser() {
        // Arrange
        String email = "find@test.com";
        User saved = createAndSaveUser(email);

        // Act
        Optional<User> result = userRepository.findByEmail(email);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("findByEmail() возвращает пустой Optional, если пользователя нет")
    void findByEmail_nonExistingEmail_returnsEmpty() {
        Optional<User> result = userRepository.findByEmail("no-such@example.com");
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("deleteUserByEmail() удаляет пользователя по email")
    void deleteUserByEmail_existingEmail_deletesUser() {
        // Arrange
        String email = "delete@test.com";
        createAndSaveUser(email);
        assertThat(userRepository.findByEmail(email)).isPresent();

        // Act
        userRepository.deleteUserByEmail(email);

        // Assert
        assertThat(userRepository.findByEmail(email)).isNotPresent();
    }
}