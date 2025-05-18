package com.localhost.pitchperfect.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Data Transfer Object for chat messages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    
    /**
     * Unique identifier for the message.
     */
    private String id;
    
    /**
     * The content of the message.
     */
    private String content;
    
    /**
     * Information about the sender.
     */
    private SenderDto sender;
    
    /**
     * The timestamp when the message was sent.
     */
    private Instant timestamp;
    
    /**
     * The type of message.
     */
    private MessageType type;
    
    /**
     * Nested class for sender information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SenderDto {
        private String id;
        private String username;
        private String avatar;
    }
    
    /**
     * Enum representing different message types.
     */
    public enum MessageType {
        TEXT,
        IMAGE,
        SYSTEM
    }
}
