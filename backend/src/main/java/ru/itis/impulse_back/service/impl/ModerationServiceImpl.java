package ru.itis.impulse_back.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.dto.response.AccountModerationResponse;
import ru.itis.impulse_back.mapper.AccountMapper;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.ModerationService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Override
    public List<AccountModerationResponse> getAllUsers() {
        return accountMapper.toResponseList(userRepository.findAll());
    }

    @Override
    public void updateUserAuthorityByEmail(String email, String authority) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User userAcc = user.get();
            userAcc.setAuthority(User.UserAuthority.valueOf(authority));
            userRepository.save(userAcc);
        }

    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        userRepository.deleteUserByEmail(email);
    }
}
