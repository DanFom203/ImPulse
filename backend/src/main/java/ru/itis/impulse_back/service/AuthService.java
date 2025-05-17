package ru.itis.impulse_back.service;

import ru.itis.impulse_back.dto.request.LoginRequest;
import ru.itis.impulse_back.dto.request.RegisterRequest;

public interface AuthService {
    String register(RegisterRequest registerRequest);
    String login(LoginRequest loginRequest);
}
