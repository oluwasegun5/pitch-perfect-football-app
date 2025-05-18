package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.application.dto.ChatMessageDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatPersistenceAdapterTest {

    @Mock
    private ChatMessageJpaRepository chatMessageRepository;

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private ChatPersistenceAdapter chatPersistenceAdapter;

    private ChatMessageEntity chatMessageEntity;
    private ChatMessageDto chatMessageDto;
    private String roomId;
    private String userId;
    private String messageId;

    @BeforeEach
    void setUp() {
        roomId = "room-123";
        userId = "user-456";
        messageId = UUID.randomUUID().toString();

        // Setup test message DTO
        chatMessageDto = new ChatMessageDto();
        chatMessageDto.setId(messageId);
        chatMessageDto.setContent("Hello, world!");
        chatMessageDto.setTimestamp(Instant.now());
        chatMessageDto.setSender(new ChatMessageDto.SenderDto(userId, "testUser", "avatar.jpg"));

        // Setup test message entity
        chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setId(messageId);
        chatMessageEntity.setRoomId(roomId);
        chatMessageEntity.setContent("Hello, world!");
        chatMessageEntity.setTimestamp(chatMessageDto.getTimestamp());
        chatMessageEntity.setSenderId(userId);
        chatMessageEntity.setSenderName("testUser");
        chatMessageEntity.setSenderAvatar("avatar.jpg");
    }

    @Test
    void saveMessage_shouldSaveAndReturnMessage() {
        // Arrange
        when(chatMessageRepository.save(any(ChatMessageEntity.class))).thenReturn(chatMessageEntity);

        // Act
        ChatMessageDto result = chatPersistenceAdapter.saveMessage(roomId, chatMessageDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(messageId);
        assertThat(result.getContent()).isEqualTo("Hello, world!");
        assertThat(result.getSender().getId()).isEqualTo(userId);
        verify(chatMessageRepository, times(1)).save(any(ChatMessageEntity.class));
    }

    @Test
    void getUsernameById_shouldReturnUsername() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername("testUser");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        String result = chatPersistenceAdapter.getUsernameById(userId);

        // Assert
        assertThat(result).isEqualTo("testUser");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUsernameById_shouldReturnUnknownUserWhenNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        String result = chatPersistenceAdapter.getUsernameById(userId);

        // Assert
        assertThat(result).isEqualTo("Unknown User");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserAvatarById_shouldReturnAvatarUrl() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setAvatarUrl("avatar.jpg");
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Act
        String result = chatPersistenceAdapter.getUserAvatarById(userId);

        // Assert
        assertThat(result).isEqualTo("avatar.jpg");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserAvatarById_shouldReturnDefaultAvatarWhenNotFound() {
        // Arrange
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        String result = chatPersistenceAdapter.getUserAvatarById(userId);

        // Assert
        assertThat(result).isEqualTo("default-avatar.jpg");
        verify(userRepository, times(1)).findById(userId);
    }
}
