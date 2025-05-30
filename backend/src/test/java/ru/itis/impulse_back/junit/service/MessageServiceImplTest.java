package ru.itis.impulse_back.junit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.itis.impulse_back.dto.request.SendMessageRequest;
import ru.itis.impulse_back.dto.response.MessageResponse;
import ru.itis.impulse_back.model.Chat;
import ru.itis.impulse_back.model.Message;
import ru.itis.impulse_back.model.User;
import ru.itis.impulse_back.repository.ChatRepository;
import ru.itis.impulse_back.repository.MessageRepository;
import ru.itis.impulse_back.repository.UserRepository;
import ru.itis.impulse_back.service.impl.MessageServiceImpl;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    void saveMessage_ShouldReturnMessageResponse_WhenChatExists() {
        Long senderId = 1L;
        Long receiverId = 2L;
        String body = "Hello!";

        SendMessageRequest request = new SendMessageRequest();
        request.setReceiverId(receiverId);
        request.setBody(body);

        User sender = new User();
        sender.setId(senderId);
        User receiver = new User();
        receiver.setId(receiverId);

        Chat existingChat = Chat.builder()
                .firstParticipant(sender)
                .secondParticipant(receiver)
                .build();

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(senderId, receiverId))
                .thenReturn(Optional.of(existingChat));

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageResponse response = messageService.saveMessage(request, senderId);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(body);
        assertThat(response.getSenderId()).isEqualTo(senderId);
        assertThat(response.getReceiverId()).isEqualTo(receiverId);

        verify(chatRepository).findByFirstParticipantIdAndSecondParticipantId(senderId, receiverId);
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void saveMessage_ShouldCreateNewChat_WhenChatDoesNotExist() {
        Long senderId = 1L;
        Long receiverId = 2L;
        String body = "Hi!";

        SendMessageRequest request = new SendMessageRequest();
        request.setReceiverId(receiverId);
        request.setBody(body);

        User sender = new User();
        sender.setId(senderId);
        User receiver = new User();
        receiver.setId(receiverId);

        Chat newChat = Chat.builder()
                .firstParticipant(sender)
                .secondParticipant(receiver)
                .build();

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(senderId, receiverId))
                .thenReturn(Optional.empty());

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(receiverId, senderId))
                .thenReturn(Optional.empty());

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));

        when(chatRepository.save(any(Chat.class))).thenReturn(newChat);
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MessageResponse response = messageService.saveMessage(request, senderId);

        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(body);
        assertThat(response.getSenderId()).isEqualTo(senderId);
        assertThat(response.getReceiverId()).isEqualTo(receiverId);

        verify(chatRepository).save(any(Chat.class));
        verify(messageRepository).save(any(Message.class));
    }

    @Test
    void saveMessage_ShouldThrowException_WhenSenderNotFound() {
        Long senderId = 1L;
        Long receiverId = 2L;
        String body = "Hello!";

        SendMessageRequest request = new SendMessageRequest();
        request.setReceiverId(receiverId);
        request.setBody(body);

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(senderId, receiverId))
                .thenReturn(Optional.empty());

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(receiverId, senderId))
                .thenReturn(Optional.empty());

        when(userRepository.findById(senderId)).thenReturn(Optional.empty());

        try {
            messageService.saveMessage(request, senderId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NoSuchElementException.class);
        }

        verify(chatRepository, never()).save(any());
        verify(messageRepository, never()).save(any());
    }

    @Test
    void saveMessage_ShouldThrowException_WhenReceiverNotFound() {
        Long senderId = 1L;
        Long receiverId = 2L;
        String body = "Hello!";

        SendMessageRequest request = new SendMessageRequest();
        request.setReceiverId(receiverId);
        request.setBody(body);

        User sender = new User();
        sender.setId(senderId);

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(senderId, receiverId))
                .thenReturn(Optional.empty());

        when(chatRepository.findByFirstParticipantIdAndSecondParticipantId(receiverId, senderId))
                .thenReturn(Optional.empty());

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.empty());

        try {
            messageService.saveMessage(request, senderId);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(NoSuchElementException.class);
        }

        verify(chatRepository, never()).save(any());
        verify(messageRepository, never()).save(any());
    }
}
