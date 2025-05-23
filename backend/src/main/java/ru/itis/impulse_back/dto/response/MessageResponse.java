package ru.itis.impulse_back.dto.response;


import lombok.*;
import ru.itis.impulse_back.model.Message;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long chatId;
    private String body;
    private Date createdAt;

    public MessageResponse(Message message) {
        this.id = message.getId();
        this.senderId = message.getSender().getId();
        this.receiverId = message.getReceiver().getId();
        this.chatId = message.getChat().getId();
        this.body = message.getBody();
        this.createdAt = message.getCreatedAt();
    }
}
