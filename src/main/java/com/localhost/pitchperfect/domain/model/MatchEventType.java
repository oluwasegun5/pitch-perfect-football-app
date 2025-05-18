package com.localhost.pitchperfect.domain.model;

/**
 * Enum representing the possible types of match events.
 */
public enum MatchEventType {
    MATCH_START,
    MATCH_END,
    MATCH_CANCELLED,
    GOAL,
    OWN_GOAL,
    YELLOW_CARD,
    RED_CARD,
    SUBSTITUTION,
    PENALTY_AWARDED,
    PENALTY_MISSED,
    PENALTY_SAVED,
    CORNER,
    FREE_KICK,
    INJURY,
    OFFSIDE,
    RESCHEDULED,
    VENUE_CHANGE
}
