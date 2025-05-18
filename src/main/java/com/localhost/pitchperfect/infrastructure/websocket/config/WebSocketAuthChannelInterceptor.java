package com.localhost.pitchperfect.infrastructure.websocket.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.localhost.pitchperfect.infrastructure.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * WebSocket authentication channel interceptor.
 * Validates JWT tokens for WebSocket connections.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = extractToken(accessor);
            
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);
                log.debug("WebSocket connection authenticated for user: {}", auth.getName());
            } else {
                log.warn("Invalid or missing authentication token for WebSocket connection");
            }
        }

        return message;
    }

    private String extractToken(StompHeaderAccessor accessor) {
        // Try to extract from header first
        String token = accessor.getFirstNativeHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        
        // If not in header, try session attributes
        if (accessor.getSessionAttributes() != null) {
            token = (String) accessor.getSessionAttributes().get("token");
            if (token != null) {
                return token;
            }
        }
        
        return null;
    }
}
