package ru.itis.impulse_back.service;

import ru.itis.impulse_back.dto.response.AccountModerationResponse;

import java.util.List;

public interface ModerationService {
    List<AccountModerationResponse> getAllUsers();
    void updateUserAuthorityByEmail(String email, String authority);
    void deleteUserByEmail(String email);

}
