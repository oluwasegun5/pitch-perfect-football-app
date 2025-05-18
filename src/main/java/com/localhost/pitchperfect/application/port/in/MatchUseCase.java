package com.localhost.pitchperfect.application.port.in;

import com.localhost.pitchperfect.application.dto.MatchDto;
import com.localhost.pitchperfect.application.dto.MatchEventDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Input port for match-related use cases.
 * This is a port in the hexagonal architecture that defines the API for the application layer.
 */
public interface MatchUseCase {
    
    /**
     * Get a match by ID.
     *
     * @param matchId the match ID
     * @return the match DTO
     */
    MatchDto getMatchById(UUID matchId);
    
    /**
     * Get all matches.
     *
     * @return a list of all matches
     */
    List<MatchDto> getAllMatches();
    
    /**
     * Get matches by status.
     *
     * @param status the match status
     * @return a list of matches with the given status
     */
    List<MatchDto> getMatchesByStatus(String status);
    
    /**
     * Get matches scheduled between the given start and end times.
     *
     * @param startTime the start time
     * @param endTime the end time
     * @return a list of matches scheduled between the given times
     */
    List<MatchDto> getMatchesByTimeRange(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Get matches involving a specific team (either home or away).
     *
     * @param teamId the team ID
     * @return a list of matches involving the given team
     */
    List<MatchDto> getMatchesByTeam(UUID teamId);
    
    /**
     * Create a new match.
     *
     * @param homeTeamId the home team ID
     * @param awayTeamId the away team ID
     * @param venue the venue
     * @param startTime the start time
     * @return the created match DTO
     */
    MatchDto createMatch(UUID homeTeamId, UUID awayTeamId, String venue, LocalDateTime startTime);
    
    /**
     * Start a match.
     *
     * @param matchId the match ID
     * @return the updated match DTO
     */
    MatchDto startMatch(UUID matchId);
    
    /**
     * Complete a match.
     *
     * @param matchId the match ID
     * @return the updated match DTO
     */
    MatchDto completeMatch(UUID matchId);
    
    /**
     * Cancel a match.
     *
     * @param matchId the match ID
     * @param reason the reason for cancellation
     * @return the updated match DTO
     */
    MatchDto cancelMatch(UUID matchId, String reason);
    
    /**
     * Update the score of a match.
     *
     * @param matchId the match ID
     * @param homeScore the home team score
     * @param awayScore the away team score
     * @return the updated match DTO
     */
    MatchDto updateScore(UUID matchId, int homeScore, int awayScore);
    
    /**
     * Add a goal to a match.
     *
     * @param matchId the match ID
     * @param scorerId the scorer ID
     * @param assistantId the assistant ID (can be null)
     * @param isHomeTeam whether the goal is for the home team
     * @return the updated match DTO
     */
    MatchDto addGoal(UUID matchId, UUID scorerId, UUID assistantId, boolean isHomeTeam);
    
    /**
     * Get events for a match.
     *
     * @param matchId the match ID
     * @return a list of match event DTOs
     */
    List<MatchEventDto> getMatchEvents(UUID matchId);
    
    /**
     * Process a match event.
     *
     * @param matchId the match ID
     * @param eventDto the match event DTO
     * @param userId the user ID
     * @return the processed match event DTO
     */
    MatchEventDto processMatchEvent(String matchId, MatchEventDto eventDto, String userId);
}
