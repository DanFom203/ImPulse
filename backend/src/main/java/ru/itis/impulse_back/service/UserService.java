package ru.itis.impulse_back.service;

import org.springframework.web.multipart.MultipartFile;
import ru.itis.impulse_back.dto.UserDto;
import ru.itis.impulse_back.dto.response.UserDetailsResponse;
import ru.itis.impulse_back.model.User;

public interface UserService {
    void create(User user);

    UserDto getByEmail(String email);

    void deleteAccount(Long userId);

    UserDetailsResponse updateUserProfilePhoto(Long userId, MultipartFile file);
}
