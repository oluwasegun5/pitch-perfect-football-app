package com.localhost.pitchperfect.infrastructure.websocket.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.localhost.pitchperfect.application.port.in.PresenceUseCase;
import com.localhost.pitchperfect.application.dto.PresenceStatusDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

/**
 * WebSocket presence channel interceptor.
 * Tracks user presence in WebSocket sessions.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketPresenceChannelInterceptor implements ChannelInterceptor {

    private final PresenceUseCase presenceUseCase;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            Principal user = accessor.getUser();
            
            if (user != null) {
                String userId = user.getName();
                
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    log.debug("User connected: {}", userId);
                    presenceUseCase.userConnected(userId);
                } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
                    log.debug("User disconnected: {}", userId);
                    presenceUseCase.userDisconnected(userId);
                } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    String destination = accessor.getDestination();
                    if (destination != null && destination.startsWith("/topic/")) {
                        String roomId = extractRoomId(destination);
                        if (roomId != null) {
                            log.debug("User {} joined room: {}", userId, roomId);
                            presenceUseCase.userJoinedRoom(userId, roomId);
                        }
                    }
                } else if (StompCommand.UNSUBSCRIBE.equals(accessor.getCommand())) {
                    String subscriptionId = accessor.getSubscriptionId();
                    if (subscriptionId != null) {
                        String roomId = presenceUseCase.getRoomIdBySubscription(userId, subscriptionId);
                        if (roomId != null) {
                            log.debug("User {} left room: {}", userId, roomId);
                            presenceUseCase.userLeftRoom(userId, roomId);
                        }
                    }
                }
            }
        }

        return message;
    }

    private String extractRoomId(String destination) {
        // Extract room ID from topic destinations like /topic/chat/{roomId} or /topic/matches/{matchId}
        if (destination.startsWith("/topic/chat/")) {
            return destination.substring("/topic/chat/".length());
        } else if (destination.startsWith("/topic/matches/")) {
            return destination.substring("/topic/matches/".length());
        }
        return null;
    }
}
