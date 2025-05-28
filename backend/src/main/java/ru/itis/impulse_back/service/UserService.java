package ru.itis.impulse_back.service;

import ru.itis.impulse_back.dto.UserDto;
import ru.itis.impulse_back.model.User;

public interface UserService {
    void create(User user);

    UserDto getByEmail(String email);

    void deleteAccount(Long userId);
}
