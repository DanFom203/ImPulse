package ru.itis.impulse_back.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.exception.UserNotFoundException;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.details.UserDetailsImpl;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            throw new UserNotFoundException("User with this email not found");
        }

        return new UserDetailsImpl(user.get());
    }
}
