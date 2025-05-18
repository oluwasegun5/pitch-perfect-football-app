package com.localhost.pitchperfect.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MatchEvent entity representing an event that occurred during a match.
 * Core domain entity for tracking match events.
 */
@Getter
@Setter
public class MatchEvent {
    private UUID id;
    private UUID matchId;
    private MatchEventType type;
    private String description;
    private Player primaryPlayer;
    private Player secondaryPlayer;
    private LocalDateTime timestamp;
    private int matchMinute;
    private String userId;
    private Map<String, String> data = new HashMap<>();

    public MatchEvent() {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.matchMinute = calculateMatchMinute();
    }

    public MatchEvent(MatchEventType type, String description, Player primaryPlayer, Player secondaryPlayer) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.description = description;
        this.primaryPlayer = primaryPlayer;
        this.secondaryPlayer = secondaryPlayer;
        this.timestamp = LocalDateTime.now();
        this.matchMinute = calculateMatchMinute();
    }

    /**
     * Calculate the match minute based on the current time.
     * This is a simplified implementation and would need to be enhanced
     * with actual match start time in a real application.
     */
    private int calculateMatchMinute() {
        // In a real implementation, this would calculate based on match start time
        // For now, we'll just return a placeholder value
        return 0;
    }
}
