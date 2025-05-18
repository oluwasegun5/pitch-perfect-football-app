package com.localhost.pitchperfect.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Data Transfer Object for typing indicators in chat.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypingIndicatorDto {
    
    /**
     * The ID of the user who is typing.
     */
    private String userId;
    
    /**
     * The username of the user who is typing.
     */
    private String username;
    
    /**
     * Whether the user is currently typing.
     */
    private boolean typing;
    
    /**
     * The timestamp when the typing status was updated.
     */
    private Instant timestamp;
}
