package ru.itis.impulse_back.junit.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.itis.impulse_back.controller.ModerationController;
import ru.itis.impulse_back.dto.request.DeleteUserRequest;
import ru.itis.impulse_back.dto.request.UpdateAuthorityRequest;
import ru.itis.impulse_back.dto.response.AccountModerationResponse;
import ru.itis.impulse_back.service.ModerationService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModerationControllerTest {

    private ModerationService moderationService;
    private ModerationController moderationController;

    @BeforeEach
    void setUp() {
        moderationService = mock(ModerationService.class);
        moderationController = new ModerationController(moderationService);
    }

    @Test
    void getAllUserAccounts_shouldReturnListOfAccounts() {
        List<AccountModerationResponse> mockAccounts = List.of(
                new AccountModerationResponse(),
                new AccountModerationResponse()
        );

        when(moderationService.getAllUsers()).thenReturn(mockAccounts);

        ResponseEntity<List<AccountModerationResponse>> response = moderationController.getAllUserAccounts();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("user1@mail.com", response.getBody().get(0).getEmail());
    }

    @Test
    void updateAuthority_shouldCallService() {
        UpdateAuthorityRequest request = new UpdateAuthorityRequest("test@mail.com", "MODERATOR");

        ResponseEntity<Void> response = moderationController.updateAuthority(request);

        verify(moderationService).updateUserAuthorityByEmail("test@mail.com", "MODERATOR");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void deleteUser_shouldCallService() {
        DeleteUserRequest request = new DeleteUserRequest("delete@mail.com");
        String token = "Bearer testtoken";

        ResponseEntity<Void> response = moderationController.deleteUser(token, request);

        verify(moderationService).deleteUserByEmail("delete@mail.com");
        assertEquals(200, response.getStatusCodeValue());
    }
}
