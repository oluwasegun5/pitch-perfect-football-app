package com.localhost.pitchperfect.domain.service;

import com.localhost.pitchperfect.domain.model.Match;
import com.localhost.pitchperfect.domain.model.MatchEvent;
import com.localhost.pitchperfect.domain.model.Player;

/**
 * Domain service for managing match operations.
 * Contains core business logic related to matches.
 */
public interface MatchDomainService {
    
    /**
     * Start a match, changing its status to LIVE.
     *
     * @param match the match to start
     * @return the updated match
     */
    Match startMatch(Match match);
    
    /**
     * Complete a match, changing its status to COMPLETED.
     *
     * @param match the match to complete
     * @return the updated match
     */
    Match completeMatch(Match match);
    
    /**
     * Cancel a match, changing its status to CANCELLED.
     *
     * @param match the match to cancel
     * @param reason the reason for cancellation
     * @return the updated match
     */
    Match cancelMatch(Match match, String reason);
    
    /**
     * Add a goal to a match.
     *
     * @param match the match
     * @param scorer the player who scored
     * @param assistant the player who assisted (can be null)
     * @param isHomeTeam whether the goal is for the home team
     * @return the updated match
     */
    Match addGoal(Match match, Player scorer, Player assistant, boolean isHomeTeam);
    
    /**
     * Add an event to a match.
     *
     * @param match the match
     * @param event the event to add
     * @return the updated match
     */
    Match addEvent(Match match, MatchEvent event);
    
    /**
     * Update the score of a match.
     *
     * @param match the match
     * @param homeScore the home team score
     * @param awayScore the away team score
     * @return the updated match
     */
    Match updateScore(Match match, int homeScore, int awayScore);
}
