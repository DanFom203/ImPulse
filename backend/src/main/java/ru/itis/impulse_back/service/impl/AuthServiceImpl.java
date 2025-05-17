package ru.itis.impulse_back.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.dto.request.LoginRequest;
import ru.itis.impulse_back.dto.request.RegisterRequest;
import ru.itis.impulse_back.exception.PasswordsNotMatchException;
import ru.itis.impulse_back.exception.UserNotFoundException;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.AuthService;
import ru.itis.impulse_back.service.UserService;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public String register(RegisterRequest registerRequest) {

        if (!registerRequest.getPassword().equals(registerRequest.getPasswordRepeat())) {
            throw new PasswordsNotMatchException("Passwords do not match");
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .fullName(registerRequest.getFullName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .createdAt(new Date())
                .role(User.UserRole.valueOf(registerRequest.getRole()))
                .authority(User.UserAuthority.DEFAULT)
                .build();

        userService.create(user);

        return jwtService.generateToken(user);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                (() -> new UserNotFoundException("User with this email not found"))
        );

        return jwtService.generateToken(user);
    }
}
