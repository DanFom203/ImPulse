package ru.itis.impulse_back.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;
import ru.itis.impulse_back.dto.MessageDto;
import ru.itis.impulse_back.dto.request.SendMessageRequest;
import ru.itis.impulse_back.dto.response.MessageResponse;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.Message;
import ru.itis.impulse_back.repository.ChatRepository;
import ru.itis.impulse_back.repository.MessageRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.MessageService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ChatController {
    @Autowired
    private SimpUserRegistry simpUserRegistry;


    private final SimpMessagingTemplate simpMessagingTemplate;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageService messageService;

    private final MessageRepository messageRepository;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload SendMessageRequest request,
            @Header("Authorization") String tokenHeader
    ) {
        if (tokenHeader == null || tokenHeader.isEmpty()) {
            throw new RuntimeException("Authorization header is missing");
        }
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;
        Long senderId = jwtService.getClaims(token).get("id").asLong();

        MessageResponse messageResponse = messageService.saveMessage(request, senderId);


        simpMessagingTemplate.convertAndSendToUser(request.getReceiverId().toString(),
                "/queue/reply", messageResponse
        );

        simpMessagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/reply",
                messageResponse
        );

    }

    @GetMapping("/api/chat/history")
    public List<MessageDto> getChatHistory(
            @RequestHeader("Authorization") String tokenHeader,
            @RequestParam("interlocutorId") Long interlocutorId
    ) {
        if (tokenHeader == null || tokenHeader.isEmpty()) {
            throw new RuntimeException("Authorization header is missing");
        }
        String token = tokenHeader.startsWith("Bearer ") ? tokenHeader.substring(7) : tokenHeader;
        Long currentUserId = jwtService.getClaims(token).get("id").asLong();

        Optional<Chat> chatOpt = chatRepository.findByFirstParticipantIdAndSecondParticipantId(currentUserId, interlocutorId);

        if (!chatOpt.isPresent()) {
            chatOpt = chatRepository.findByFirstParticipantIdAndSecondParticipantId(interlocutorId, currentUserId);
        }

        if (chatOpt.isEmpty()) {
            return List.of();
        }
        Chat chat = chatOpt.get();
        List<Message> messages = messageRepository.findAllByChatOrderByCreatedAtAsc(chat);
        System.out.println(messages);
        return messages.stream().map(MessageDto::from).toList();
    }
    @GetMapping("/api/chat/message/{id}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long id) {
        Optional<Message> msg = messageRepository.findById(id);
        if (msg.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(MessageDto.from(msg.get()));
    }


}
