package com.localhost.pitchperfect.domain.model;

/**
 * Enum representing the possible positions of a football player.
 */
public enum Position {
    GOALKEEPER("GK", "Goalkeeper"),
    DEFENDER("DEF", "Defender"),
    MIDFIELDER("MID", "Midfielder"),
    FORWARD("FWD", "Forward");
    
    private final String shortCode;
    private final String displayName;
    
    Position(String shortCode, String displayName) {
        this.shortCode = shortCode;
        this.displayName = displayName;
    }
    
    public String getShortCode() {
        return shortCode;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
