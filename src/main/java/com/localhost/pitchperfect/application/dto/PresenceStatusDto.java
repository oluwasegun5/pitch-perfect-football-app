package com.localhost.pitchperfect.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Data Transfer Object for presence status information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresenceStatusDto {
    
    /**
     * The user ID.
     */
    private String userId;
    
    /**
     * The username for display.
     */
    private String username;
    
    /**
     * The status of the user.
     */
    private PresenceStatus status;
    
    /**
     * The timestamp when the status was updated.
     */
    private Instant timestamp;
    
    /**
     * Enum representing different presence statuses.
     */
    public enum PresenceStatus {
        ONLINE,
        OFFLINE,
        AWAY,
        BUSY,
        JOINED,
        LEFT
    }
}
