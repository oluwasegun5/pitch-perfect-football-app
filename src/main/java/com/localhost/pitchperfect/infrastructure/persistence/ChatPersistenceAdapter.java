package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.application.dto.ChatMessageDto;
import com.localhost.pitchperfect.application.port.out.ChatPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of the ChatPersistencePort.
 * Provides persistence operations for chat functionality.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPersistenceAdapter implements ChatPersistencePort {

    private final ChatMessageJpaRepository chatMessageRepository;
    private final UserJpaRepository userRepository;
    
    private static final String DEFAULT_AVATAR = "default-avatar.jpg";

    @Override
    public ChatMessageDto saveMessage(String roomId, ChatMessageDto message) {
        // Convert DTO to entity
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setId(message.getId());
        entity.setRoomId(roomId);
        entity.setContent(message.getContent());
        entity.setTimestamp(message.getTimestamp());
        entity.setSenderId(message.getSender().getId());
        entity.setSenderName(message.getSender().getUsername());
        entity.setSenderAvatar(message.getSender().getAvatar());
        
        // Save entity
        ChatMessageEntity savedEntity = chatMessageRepository.save(entity);
        
        // Convert back to DTO
        return toDto(savedEntity);
    }

    @Override
    public String getUsernameById(String userId) {
        return userRepository.findById(userId)
                .map(UserEntity::getUsername)
                .orElse("Unknown User");
    }

    @Override
    public String getUserAvatarById(String userId) {
        return userRepository.findById(userId)
                .map(UserEntity::getAvatarUrl)
                .orElse(DEFAULT_AVATAR);
    }
    
    /**
     * Converts a ChatMessageEntity to a ChatMessageDto.
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    private ChatMessageDto toDto(ChatMessageEntity entity) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setTimestamp(entity.getTimestamp());
        
        ChatMessageDto.SenderDto sender = new ChatMessageDto.SenderDto(
                entity.getSenderId(),
                entity.getSenderName(),
                entity.getSenderAvatar()
        );
        dto.setSender(sender);
        
        return dto;
    }
}
