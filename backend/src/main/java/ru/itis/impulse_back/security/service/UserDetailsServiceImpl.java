package ru.itis.impulse_back.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.exception.UserNotFoundException;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.details.UserDetailsImpl;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user by email: {}", username);
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            log.warn("User with email [{}] not found", username);
            throw new UserNotFoundException("User with this email not found");
        }
        log.debug("User [{}] loaded successfully", username);
        return new UserDetailsImpl(user.get());
    }
}
