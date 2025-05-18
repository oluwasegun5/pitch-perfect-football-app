package com.localhost.pitchperfect.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Match entity representing a football match in the domain layer.
 * Core domain entity with business logic and invariants.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Match {
    private UUID id;
    private Team homeTeam;
    private Team awayTeam;
    private String venue;
    private LocalDateTime startTime;
    private MatchStatus status;
    private int homeScore;
    private int awayScore;
    private List<MatchEvent> events;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Match(Team homeTeam, Team awayTeam, String venue, LocalDateTime startTime) {
        this.id = UUID.randomUUID();
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.venue = venue;
        this.startTime = startTime;
        this.status = MatchStatus.SCHEDULED;
        this.homeScore = 0;
        this.awayScore = 0;
        this.events = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        validateTeams();
        validateStartTime();
    }

    /**
     * Start the match, changing its status to LIVE.
     * Can only be started if currently in SCHEDULED status.
     */
    public void start() {
        if (status != MatchStatus.SCHEDULED) {
            throw new IllegalStateException("Match can only be started when in SCHEDULED state");
        }
        
        this.status = MatchStatus.LIVE;
        this.updatedAt = LocalDateTime.now();
        addEvent(new MatchEvent(MatchEventType.MATCH_START, "Match started", null, null));
    }

    /**
     * Complete the match, changing its status to COMPLETED.
     * Can only be completed if currently in LIVE status.
     */
    public void complete() {
        if (status != MatchStatus.LIVE) {
            throw new IllegalStateException("Match can only be completed when in LIVE state");
        }
        
        this.status = MatchStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
        addEvent(new MatchEvent(MatchEventType.MATCH_END, "Match completed", null, null));
    }

    /**
     * Cancel the match, changing its status to CANCELLED.
     * Can be cancelled if in SCHEDULED or LIVE status.
     */
    public void cancel(String reason) {
        if (status == MatchStatus.COMPLETED || status == MatchStatus.CANCELLED) {
            throw new IllegalStateException("Match cannot be cancelled when already completed or cancelled");
        }
        
        this.status = MatchStatus.CANCELLED;
        this.updatedAt = LocalDateTime.now();
        addEvent(new MatchEvent(MatchEventType.MATCH_CANCELLED, reason, null, null));
    }

    /**
     * Update the match score.
     * Can only update score if match is LIVE.
     */
    public void updateScore(int homeScore, int awayScore) {
        if (status != MatchStatus.LIVE) {
            throw new IllegalStateException("Score can only be updated when match is LIVE");
        }
        
        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative");
        }
        
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Add a goal for the home team.
     */
    public void addHomeGoal(Player scorer, Player assistant) {
        if (status != MatchStatus.LIVE) {
            throw new IllegalStateException("Goals can only be added when match is LIVE");
        }
        
        this.homeScore++;
        this.updatedAt = LocalDateTime.now();
        addEvent(new MatchEvent(MatchEventType.GOAL, "Goal scored by " + scorer.getName(), scorer, assistant));
    }

    /**
     * Add a goal for the away team.
     */
    public void addAwayGoal(Player scorer, Player assistant) {
        if (status != MatchStatus.LIVE) {
            throw new IllegalStateException("Goals can only be added when match is LIVE");
        }
        
        this.awayScore++;
        this.updatedAt = LocalDateTime.now();
        addEvent(new MatchEvent(MatchEventType.GOAL, "Goal scored by " + scorer.getName(), scorer, assistant));
    }

    /**
     * Add a match event.
     */
    public void addEvent(MatchEvent event) {
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(event);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Reschedule the match to a new date and time.
     * Can only reschedule if match is in SCHEDULED status.
     */
    public void reschedule(LocalDateTime newStartTime) {
        if (status != MatchStatus.SCHEDULED) {
            throw new IllegalStateException("Match can only be rescheduled when in SCHEDULED state");
        }
        
        validateStartTime(newStartTime);
        this.startTime = newStartTime;
        this.updatedAt = LocalDateTime.now();
        addEvent(new MatchEvent(MatchEventType.RESCHEDULED, "Match rescheduled to " + newStartTime, null, null));
    }

    /**
     * Change the venue of the match.
     * Can only change venue if match is in SCHEDULED status.
     */
    public void changeVenue(String newVenue) {
        if (status != MatchStatus.SCHEDULED) {
            throw new IllegalStateException("Venue can only be changed when match is in SCHEDULED state");
        }
        
        if (newVenue == null || newVenue.trim().isEmpty()) {
            throw new IllegalArgumentException("Venue cannot be empty");
        }
        
        String oldVenue = this.venue;
        this.venue = newVenue;
        this.updatedAt = LocalDateTime.now();
        addEvent(new MatchEvent(MatchEventType.VENUE_CHANGE, "Venue changed from " + oldVenue + " to " + newVenue, null, null));
    }

    /**
     * Get the current match result as a string.
     */
    public String getResult() {
        return homeTeam.getName() + " " + homeScore + " - " + awayScore + " " + awayTeam.getName();
    }

    /**
     * Get the winner of the match.
     * Returns null if the match is a draw or not completed.
     */
    public Team getWinner() {
        if (status != MatchStatus.COMPLETED) {
            return null;
        }
        
        if (homeScore > awayScore) {
            return homeTeam;
        } else if (awayScore > homeScore) {
            return awayTeam;
        } else {
            return null; // Draw
        }
    }

    /**
     * Check if the match is a draw.
     */
    public boolean isDraw() {
        return status == MatchStatus.COMPLETED && homeScore == awayScore;
    }

    /**
     * Validate that home and away teams are different.
     */
    private void validateTeams() {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Both home and away teams must be specified");
        }
        
        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("Home and away teams cannot be the same");
        }
    }

    /**
     * Validate that start time is in the future.
     */
    private void validateStartTime() {
        validateStartTime(this.startTime);
    }

    /**
     * Validate that a given start time is in the future.
     */
    private void validateStartTime(LocalDateTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("Start time must be specified");
        }
        
        if (startTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Start time must be in the future");
        }
    }
}
