package com.localhost.pitchperfect.infrastructure.websocket.controller;

import com.localhost.pitchperfect.application.dto.ChatMessageDto;
import com.localhost.pitchperfect.application.dto.MatchEventDto;
import com.localhost.pitchperfect.application.dto.TypingIndicatorDto;
import com.localhost.pitchperfect.application.port.in.ChatUseCase;
import com.localhost.pitchperfect.application.port.in.MatchUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket controller for handling real-time messages.
 * Acts as an adapter in the hexagonal architecture.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {

    private final ChatUseCase chatUseCase;
    private final MatchUseCase matchUseCase;

    /**
     * Handles chat messages sent to a specific room.
     *
     * @param roomId the ID of the chat room
     * @param message the chat message
     * @param principal the authenticated user
     * @return the processed chat message
     */
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageDto sendChatMessage(
            @DestinationVariable String roomId,
            ChatMessageDto message,
            Principal principal,
            SimpMessageHeaderAccessor headerAccessor) {
        
        String userId = principal.getName();
        log.debug("Received chat message from user {} in room {}: {}", userId, roomId, message.getContent());
        
        // Process and save the message using the application service
        return chatUseCase.processAndSaveMessage(roomId, message, userId);
    }

    /**
     * Handles typing indicators in a chat room.
     *
     * @param roomId the ID of the chat room
     * @param indicator the typing indicator
     * @param principal the authenticated user
     * @return the processed typing indicator
     */
    @MessageMapping("/chat/{roomId}/typing")
    @SendTo("/topic/chat/{roomId}/typing")
    public TypingIndicatorDto sendTypingIndicator(
            @DestinationVariable String roomId,
            TypingIndicatorDto indicator,
            Principal principal) {
        
        String userId = principal.getName();
        log.debug("Received typing indicator from user {} in room {}", userId, roomId);
        
        // Set the user information in the indicator
        indicator.setUserId(userId);
        indicator.setUsername(chatUseCase.getUsernameById(userId));
        
        return indicator;
    }

    /**
     * Handles match events for a specific match.
     *
     * @param matchId the ID of the match
     * @param event the match event
     * @param principal the authenticated user
     * @return the processed match event
     */
    @MessageMapping("/matches/{matchId}/events")
    @SendTo("/topic/matches/{matchId}")
    public MatchEventDto sendMatchEvent(
            @DestinationVariable String matchId,
            MatchEventDto event,
            Principal principal) {
        
        String userId = principal.getName();
        log.debug("Received match event from user {} for match {}: {}", userId, matchId, event.getType());
        
        // Process and save the match event using the application service
        return matchUseCase.processMatchEvent(matchId, event, userId);
    }
}
