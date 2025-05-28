package ru.itis.impulse_back.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.dto.UserDto;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.exception.UserAlreadyExistsException;
import ru.itis.impulse_back.exception.UserNotFoundException;
import ru.itis.impulse_back.mapper.UserDetailsMapper;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.*;
import ru.itis.impulse_back.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final ReviewRepository reviewRepository;

    private final UserDetailsMapper userDetailsMapper;

    @Override
    public void create(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserAlreadyExistsException("User with this email already exists");
        }
    }

    @Override
    public UserDto getByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User with this email not found");
        }

        User user = userOptional.get();

        return UserDto.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .createdAt(user.getCreatedAt())
                .role(user.getRole())
                .authority(user.getAuthority())
                .profileImageUrl(user.getProfileImageUrl())
                .specialistBio(user.getSpecialistBio())
                .specialistAppointmentPrice(user.getSpecialistAppointmentPrice())
                .specialistAvgRating(user.getSpecialistAvgRating())
                .clientReviews(user.getClientReviews())
                .specialistReviews(user.getSpecialistReviews())
                .specialties(user.getSpecialties())
                .build();
    }

    @Override
    public UserDetailsResponse updateUserProfilePhoto(Long userId, String imageUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setProfileImageUrl(imageUrl);
        userRepository.save(user);

        return userDetailsMapper.toResponse(user);
    }

    @Transactional
    @Override
    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.getSpecialties().clear();
        userRepository.save(user);

        messageRepository.deleteAllBySenderOrReceiver(user, user);
        appointmentRepository.deleteAllByClientOrSpecialist(user, user);
        chatRepository.deleteAllByFirstParticipantOrSecondParticipant(user, user);
        reviewRepository.deleteAllByClientOrSpecialist(user, user);

        userRepository.delete(user);
    }

}
