package ru.itis.impulse_back.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.dto.response.AccountModerationResponse;
import ru.itis.impulse_back.exception.UserNotFoundException;
import ru.itis.impulse_back.mapper.AccountMapper;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.*;
import ru.itis.impulse_back.service.ModerationService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModerationServiceImpl implements ModerationService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ReviewRepository reviewRepository;
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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.getSpecialties().clear();
        userRepository.save(user);

        messageRepository.deleteAllBySenderOrReceiver(user, user);
        appointmentRepository.deleteAllByClientOrSpecialist(user, user);
        chatRepository.deleteAllByFirstParticipantOrSecondParticipant(user, user);
        reviewRepository.deleteAllByClientOrSpecialist(user, user);

        userRepository.deleteUserByEmail(email);
    }
}
