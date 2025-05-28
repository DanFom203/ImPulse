package ru.itis.impulse_back.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.impulse_back.service.FirebaseService;

import java.io.IOException;
import java.sql.Blob;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FirebaseServiceImpl implements FirebaseService {

    @Override
    public String uploadUserAvatar(MultipartFile file, Long userId) {
        String fileName = "avatars/" + userId + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        try {
            Blob blob = storage.create(
                    BlobInfo.newBuilder(bucketName, fileName)
                            .setContentType(file.getContentType())
                            .build(),
                    file.getBytes()
            );
            return blob.getMediaLink();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла", e);
        }
    }

}
