package ru.itis.impulse_back.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itis.impulse_back.dto.request.SendMessageRequest;
import ru.itis.impulse_back.dto.response.MessageResponse;
import ru.itis.impulse_back.model.Message;

public interface MessageService {
    MessageResponse saveMessage(SendMessageRequest request, Long senderId);
}