package com.localhost.pitchperfect.application.service;

import com.localhost.pitchperfect.application.dto.PresenceStatusDto;
import com.localhost.pitchperfect.application.port.in.PresenceUseCase;
import com.localhost.pitchperfect.application.port.out.PresencePersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the PresenceUseCase port.
 * Manages user presence tracking and notifications.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceService implements PresenceUseCase {

    private final SimpMessagingTemplate messagingTemplate;
    private final PresencePersistencePort presencePersistencePort;
    
    // In-memory mapping of subscriptionId to roomId for quick lookups
    private final Map<String, String> userSubscriptions = new ConcurrentHashMap<>();

    @Override
    public void userConnected(String userId) {
        PresenceStatusDto status = new PresenceStatusDto();
        status.setUserId(userId);
        status.setUsername(presencePersistencePort.getUsernameById(userId));
        status.setStatus(PresenceStatusDto.PresenceStatus.ONLINE);
        status.setTimestamp(Instant.now());
        
        presencePersistencePort.saveUserStatus(userId, status);
        
        // Notify relevant subscribers about user's online status
        messagingTemplate.convertAndSend("/topic/presence/global", status);
        log.debug("User connected status broadcast: {}", userId);
    }

    @Override
    public void userDisconnected(String userId) {
        PresenceStatusDto status = new PresenceStatusDto();
        status.setUserId(userId);
        status.setUsername(presencePersistencePort.getUsernameById(userId));
        status.setStatus(PresenceStatusDto.PresenceStatus.OFFLINE);
        status.setTimestamp(Instant.now());
        
        presencePersistencePort.saveUserStatus(userId, status);
        
        // Notify relevant subscribers about user's offline status
        messagingTemplate.convertAndSend("/topic/presence/global", status);
        log.debug("User disconnected status broadcast: {}", userId);
        
        // Clean up any remaining room subscriptions
        presencePersistencePort.removeAllUserSubscriptions(userId);
    }

    @Override
    public void userJoinedRoom(String userId, String roomId) {
        PresenceStatusDto status = new PresenceStatusDto();
        status.setUserId(userId);
        status.setUsername(presencePersistencePort.getUsernameById(userId));
        status.setStatus(PresenceStatusDto.PresenceStatus.JOINED);
        status.setTimestamp(Instant.now());
        
        // Save the subscription
        String subscriptionId = userId + ":" + roomId;
        userSubscriptions.put(subscriptionId, roomId);
        presencePersistencePort.saveUserSubscription(userId, subscriptionId, roomId);
        
        // Notify room members about the new user
        messagingTemplate.convertAndSend("/topic/presence/" + roomId, status);
        log.debug("User joined room broadcast: {} -> {}", userId, roomId);
    }

    @Override
    public void userLeftRoom(String userId, String roomId) {
        PresenceStatusDto status = new PresenceStatusDto();
        status.setUserId(userId);
        status.setUsername(presencePersistencePort.getUsernameById(userId));
        status.setStatus(PresenceStatusDto.PresenceStatus.LEFT);
        status.setTimestamp(Instant.now());
        
        // Remove the subscription
        String subscriptionId = userId + ":" + roomId;
        userSubscriptions.remove(subscriptionId);
        presencePersistencePort.removeUserSubscription(userId, subscriptionId);
        
        // Notify room members about the user leaving
        messagingTemplate.convertAndSend("/topic/presence/" + roomId, status);
        log.debug("User left room broadcast: {} -> {}", userId, roomId);
    }

    @Override
    public String getRoomIdBySubscription(String userId, String subscriptionId) {
        // Try local cache first
        String roomId = userSubscriptions.get(userId + ":" + subscriptionId);
        
        // If not found in local cache, try persistent storage
        if (roomId == null) {
            roomId = presencePersistencePort.getRoomIdBySubscription(userId, subscriptionId);
        }
        
        return roomId;
    }
}
