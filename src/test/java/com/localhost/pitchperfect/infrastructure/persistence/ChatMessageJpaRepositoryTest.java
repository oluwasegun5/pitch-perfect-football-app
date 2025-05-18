package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.application.dto.ChatMessageDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ChatMessageJpaRepositoryTest {

    @Mock
    private ChatMessageJpaRepository chatMessageRepository;

    @Test
    void findByRoomIdOrderByTimestampAsc_shouldReturnMessagesInOrder() {
        // Arrange
        String roomId = "room-123";
        
        ChatMessageEntity message1 = new ChatMessageEntity();
        message1.setId("msg-1");
        message1.setRoomId(roomId);
        message1.setContent("First message");
        message1.setTimestamp(Instant.now().minusSeconds(60));
        message1.setSenderId("user-1");
        message1.setSenderName("User 1");
        
        ChatMessageEntity message2 = new ChatMessageEntity();
        message2.setId("msg-2");
        message2.setRoomId(roomId);
        message2.setContent("Second message");
        message2.setTimestamp(Instant.now());
        message2.setSenderId("user-2");
        message2.setSenderName("User 2");
        
        List<ChatMessageEntity> messages = Arrays.asList(message1, message2);
        
        when(chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId)).thenReturn(messages);
        
        // Act
        List<ChatMessageEntity> result = chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("msg-1");
        assertThat(result.get(1).getId()).isEqualTo("msg-2");
        verify(chatMessageRepository).findByRoomIdOrderByTimestampAsc(roomId);
    }
    
    @Test
    void findTop50ByRoomIdOrderByTimestampDesc_shouldReturnLimitedMessages() {
        // Arrange
        String roomId = "room-123";
        
        ChatMessageEntity message1 = new ChatMessageEntity();
        message1.setId("msg-1");
        message1.setRoomId(roomId);
        message1.setContent("First message");
        message1.setTimestamp(Instant.now().minusSeconds(60));
        
        ChatMessageEntity message2 = new ChatMessageEntity();
        message2.setId("msg-2");
        message2.setRoomId(roomId);
        message2.setContent("Second message");
        message2.setTimestamp(Instant.now());
        
        List<ChatMessageEntity> messages = Arrays.asList(message2, message1); // Descending order
        
        when(chatMessageRepository.findTop50ByRoomIdOrderByTimestampDesc(roomId)).thenReturn(messages);
        
        // Act
        List<ChatMessageEntity> result = chatMessageRepository.findTop50ByRoomIdOrderByTimestampDesc(roomId);
        
        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("msg-2"); // Most recent first
        assertThat(result.get(1).getId()).isEqualTo("msg-1");
        verify(chatMessageRepository).findTop50ByRoomIdOrderByTimestampDesc(roomId);
    }
    
    @Test
    void deleteByRoomId_shouldDeleteAllMessagesInRoom() {
        // Arrange
        String roomId = "room-123";
        
        // Act
        chatMessageRepository.deleteByRoomId(roomId);
        
        // Assert
        verify(chatMessageRepository).deleteByRoomId(roomId);
    }
}
