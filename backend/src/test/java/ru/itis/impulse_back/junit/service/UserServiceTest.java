package ru.itis.impulse_back.junit.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.itis.impulse_back.dto.UserDto;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.exception.UserAlreadyExistsException;
import ru.itis.impulse_back.exception.UserNotFoundException;
import ru.itis.impulse_back.mapper.UserDetailsMapper;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.*;
import ru.itis.impulse_back.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ChatRepository chatRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private UserDetailsMapper userDetailsMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .fullName("Test User")
                .profileImageUrl("old_url.jpg")
                .specialties(new ArrayList<>())
                .build();
    }

    @Test
    void create_ShouldSaveUser_WhenNoException() {
        when(userRepository.save(user)).thenReturn(user);

        userService.create(user);

        verify(userRepository).save(user);
    }

    @Test
    void create_ShouldThrowUserAlreadyExistsException_WhenDataIntegrityViolation() {
        when(userRepository.save(user)).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.create(user))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessage("User with this email already exists");

        verify(userRepository).save(user);
    }

    @Test
    void getByEmail_ShouldReturnUserDto_WhenUserFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDto userDto = userService.getByEmail("test@example.com");

        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo("test@example.com");

        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void getByEmail_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByEmail("missing@example.com"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with this email not found");

        verify(userRepository).findByEmail("missing@example.com");
    }

    @Test
    void updateUserProfilePhoto_ShouldUpdatePhotoAndReturnResponse_WhenUserExists() {
        String newImageUrl = "new_url.jpg";
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDetailsResponse response = new UserDetailsResponse();
        when(userDetailsMapper.toResponse(any(User.class))).thenReturn(response);

        UserDetailsResponse result = userService.updateUserProfilePhoto(1L, newImageUrl);

        assertThat(user.getProfileImageUrl()).isEqualTo(newImageUrl);
        assertThat(result).isEqualTo(response);

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
        verify(userDetailsMapper).toResponse(user);
    }

    @Test
    void updateUserProfilePhoto_ShouldThrowUsernameNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUserProfilePhoto(1L, "url"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteAccount_ShouldDeleteAllRelatedDataAndUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteAccount(1L);

        verify(userRepository).findById(1L);
        verify(messageRepository).deleteAllBySenderOrReceiver(user, user);
        verify(appointmentRepository).deleteAllByClientOrSpecialist(user, user);
        verify(chatRepository).deleteAllByFirstParticipantOrSecondParticipant(user, user);
        verify(reviewRepository).deleteAllByClientOrSpecialist(user, user);
        verify(userRepository).save(user);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteAccount_ShouldThrowUserNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deleteAccount(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository, messageRepository, appointmentRepository, chatRepository, reviewRepository);
    }
}
