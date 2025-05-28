package ru.itis.impulse_back.service.impl;

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
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
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

    @Override
    public void deleteAccount(Long userId) {
        userRepository.delete(userRepository.findById(userId).get());
    }

}
