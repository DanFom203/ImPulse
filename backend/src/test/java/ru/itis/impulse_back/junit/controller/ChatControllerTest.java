package ru.itis.impulse_back.junit.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.itis.impulse_back.controller.ChatController;
import ru.itis.impulse_back.dto.MessageDto;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.Message;
import ru.itis.impulse_back.repository.ChatRepository;
import ru.itis.impulse_back.repository.MessageRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.security.service.JWTService;
import ru.itis.impulse_back.service.MessageService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MessageRepository messageRepository;


    private static final String TOKEN = "Bearer valid-token";
    private static final Long USER_ID = 1L;
    private static final Long INTERLOCUTOR_ID = 2L;

    @Test
    void getChatHistory_ShouldReturnMessages() throws Exception {
        Chat chat = Chat.builder().id(1L).build();

        Message message = Message.builder().id(1L).body("Hello").chat(chat).build();

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(USER_ID, INTERLOCUTOR_ID))
                .thenReturn(Optional.of(chat));
        when(messageRepository.findAllByChatOrderByCreatedAtAsc(chat))
                .thenReturn(List.of(message));

        mockMvc.perform(get("/api/chat/history")
                        .header("Authorization", TOKEN)
                        .param("interlocutorId", INTERLOCUTOR_ID.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(message.getId()))
                .andExpect(jsonPath("$[0].text").value("Hello"));
    }

    @Test
    void getMessageById_ShouldReturnMessage_WhenExists() throws Exception {
        Message message = Message.builder().id(1L).body("Hi!").build();
        when(messageRepository.findById(1L)).thenReturn(Optional.of(message));

        mockMvc.perform(get("/api/chat/message/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Hi!"));
    }

    @Test
    void getMessageById_ShouldReturn404_WhenNotFound() throws Exception {
        when(messageRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/chat/message/999"))
                .andExpect(status().isNotFound());
    }
}
