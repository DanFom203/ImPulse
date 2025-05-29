package ru.itis.impulse_back.junit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.impulse_back.dto.response.AccountModerationResponse;
import ru.itis.impulse_back.mapper.AccountMapper;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.impl.ModerationServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModerationServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private ModerationServiceImpl moderationService;

    @Test
    void getAllUsers_ShouldReturnMappedResponseList() {
        List<User> users = List.of(new User(), new User());
        List<AccountModerationResponse> responses = List.of(
                new AccountModerationResponse(), new AccountModerationResponse()
        );

        when(userRepository.findAll()).thenReturn(users);
        when(accountMapper.toResponseList(users)).thenReturn(responses);

        List<AccountModerationResponse> result = moderationService.getAllUsers();

        assertThat(result).isEqualTo(responses);
        verify(userRepository).findAll();
        verify(accountMapper).toResponseList(users);
    }

    @Test
    void updateUserAuthorityByEmail_ShouldUpdateAndSave_WhenUserExists() {
        String email = "test@example.com";
        String newAuthority = "ADMIN";

        User user = new User();
        user.setAuthority(User.UserAuthority.DEFAULT);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        moderationService.updateUserAuthorityByEmail(email, newAuthority);

        assertThat(user.getAuthority()).isEqualTo(User.UserAuthority.ADMIN);
        verify(userRepository).save(user);
    }

    @Test
    void updateUserAuthorityByEmail_ShouldDoNothing_WhenUserNotFound() {
        String email = "missing@example.com";
        String newAuthority = "ADMIN";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        moderationService.updateUserAuthorityByEmail(email, newAuthority);

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUserByEmail_ShouldCallRepositoryDelete() {
        String email = "delete@example.com";

        moderationService.deleteUserByEmail(email);

        verify(userRepository).deleteUserByEmail(email);
    }
}
