package ru.itis.impulse_back.dto;

import lombok.Builder;
import lombok.Data;
import ru.itis.impulse_back.model.Message;

import java.util.Date;

@Data
@Builder
public class MessageDto {
    private Long id;
    private String body;
    private Date createdAt;
    private ShortUserDto sender;
    private ShortUserDto receiver;
    private Long senderId;
    private Long receiverId;

    public static MessageDto from(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .body(message.getBody())
                .createdAt(message.getCreatedAt())
                .sender(ShortUserDto.from(message.getSender()))
                .receiver(ShortUserDto.from(message.getReceiver()))
                .senderId(message.getSender().getId())
                .receiverId(message.getReceiver().getId())
                .build();
    }
}