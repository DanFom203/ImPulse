package ru.itis.impulse_back.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private Long receiverId;
    private String body;
    private SendMessageRequest mapToSendMessageRequest(Map<String, Object> payload) {
        SendMessageRequest request = new SendMessageRequest();
        request.setReceiverId(Long.valueOf(payload.get("receiverId").toString()));
        request.setBody((String) payload.get("body"));
        return request;
    }

}

