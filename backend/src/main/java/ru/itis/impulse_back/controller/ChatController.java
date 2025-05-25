package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.impulse_back.dto.MessageDto;
import ru.itis.impulse_back.dto.request.SendMessageRequest;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.Message;
import ru.itis.impulse_back.repository.ChatRepository;
import ru.itis.impulse_back.repository.MessageRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.service.JWTService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat")
    @SendToUser("/queue/reply")
    public void processMessage(
            @Payload Map<String, Object> payload,
            @Header("Authorization") String tokenHeader
    ) {
        System.out.println("Получен токен: " + tokenHeader);

        if (tokenHeader == null || tokenHeader.isEmpty()) {
            throw new RuntimeException("Authorization header is missing");
        }

        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;

        Long receiverId = Long.valueOf(payload.get("receiverId").toString());
        String body = (String) payload.get("body");

        Long senderId = jwtService.getClaims(token).get("id").asLong();

        Chat chat;
        Optional<Chat> chatFromDb = chatRepository.findByFirstParticipantIdAndSecondParticipantId(senderId, receiverId);

        if (chatFromDb.isPresent()) {
            chat = chatFromDb.get();
        } else {
            chatFromDb = chatRepository.findByFirstParticipantIdAndSecondParticipantId(receiverId, senderId);
            if (chatFromDb.isPresent()) {
                chat = chatFromDb.get();
            } else {
                chat = Chat.builder()
                        .firstParticipant(userRepository.findById(senderId).get())
                        .secondParticipant(userRepository.findById(receiverId).get())
                        .build();
                chatRepository.save(chat);
            }
        }

        Message message = Message.builder()
                .chat(chat)
                .sender(userRepository.findById(senderId).get())
                .receiver(userRepository.findById(receiverId).get())
                .body(body)
                .createdAt(new Date())
                .build();
        messageRepository.save(message);
        MessageDto messageDto = MessageDto.from(message);

        simpMessagingTemplate.convertAndSendToUser(
                receiverId.toString(),
                "/queue/reply",
                messageDto
        );
    }


}
