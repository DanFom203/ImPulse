package ru.itis.impulse_back.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.impulse_back.dto.request.SendMessageRequest;
import ru.itis.impulse_back.dto.response.MessageResponse;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.Message;
import ru.itis.impulse_back.repository.ChatRepository;
import ru.itis.impulse_back.repository.MessageRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.MessageService;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @Override
    public MessageResponse saveMessage(SendMessageRequest request, Long senderId) {
        Chat chat = chatRepository
                .findByFirstParticipantIdAndSecondParticipantId(senderId, request.getReceiverId())
                .orElseGet(() -> {
                    Chat newChat = Chat.builder()
                            .firstParticipant(userRepository.findById(senderId).get())
                            .secondParticipant(userRepository.findById(request.getReceiverId()).get())
                            .build();
                    return chatRepository.save(newChat);
                });

        Message message = Message.builder()
                .chat(chat)
                .sender(userRepository.findById(senderId).get())
                .receiver(userRepository.findById(request.getReceiverId()).get())
                .body(request.getBody())
                .createdAt(new Date())
                .build();

        messageRepository.save(message);

        return new MessageResponse(message);
    }
}

