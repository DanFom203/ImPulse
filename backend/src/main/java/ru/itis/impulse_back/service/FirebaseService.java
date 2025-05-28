package ru.itis.impulse_back.service;

import org.springframework.web.multipart.MultipartFile;

public interface FirebaseService {
    String uploadUserAvatar(MultipartFile file, Long userId);
}
