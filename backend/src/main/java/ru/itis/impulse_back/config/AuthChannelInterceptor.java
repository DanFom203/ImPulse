package ru.itis.impulse_back.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class AuthChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(@NotNull Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String authHeader = accessor.getFirstNativeHeader("Authorization");

        if (authHeader != null) {
            accessor.setHeader("Authorization", authHeader); // <<< ВАЖНО: добавляем в headers для контроллера
            System.out.println("Interceptor Authorization header: " + authHeader);
        }

        return message;
    }


}
