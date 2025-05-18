package com.localhost.pitchperfect.application.service;

import com.localhost.pitchperfect.application.dto.ChatMessageDto;
import com.localhost.pitchperfect.application.port.in.ChatUseCase;
import com.localhost.pitchperfect.application.port.out.ChatPersistencePort;
import com.localhost.pitchperfect.domain.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Implementation of the ChatUseCase port.
 * Manages chat message processing and persistence.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService implements ChatUseCase {

    private final ChatPersistencePort chatPersistencePort;
    private final DomainEventPublisher eventPublisher;

    @Override
    public ChatMessageDto processAndSaveMessage(String roomId, ChatMessageDto message, String userId) {
        // Set message metadata if not already set
        if (message.getId() == null) {
            message.setId(UUID.randomUUID().toString());
        }
        
        if (message.getTimestamp() == null) {
            message.setTimestamp(Instant.now());
        }
        
        // Set sender information
        String username = getUsernameById(userId);
        String avatar = chatPersistencePort.getUserAvatarById(userId);
        
        message.setSender(new ChatMessageDto.SenderDto(userId, username, avatar));
        
        // Save the message
        ChatMessageDto savedMessage = chatPersistencePort.saveMessage(roomId, message);
        
        // Publish domain event for the new message
        eventPublisher.publish("chat.message.created", savedMessage);
        
        log.debug("Processed and saved chat message: {} in room: {}", message.getId(), roomId);
        
        return savedMessage;
    }

    @Override
    public String getUsernameById(String userId) {
        return chatPersistencePort.getUsernameById(userId);
    }
}
