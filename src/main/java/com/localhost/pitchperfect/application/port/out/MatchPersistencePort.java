package com.localhost.pitchperfect.application.port.out;

import com.localhost.pitchperfect.domain.model.Match;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Output port for match persistence operations.
 * This is a port in the hexagonal architecture that will be implemented by adapters.
 */
public interface MatchPersistencePort {
    
    /**
     * Find a match by its ID.
     *
     * @param id the match ID
     * @return an Optional containing the match if found, or empty if not found
     */
    Optional<Match> findById(UUID id);
    
    /**
     * Find all matches.
     *
     * @return a list of all matches
     */
    List<Match> findAll();
    
    /**
     * Find matches by status.
     *
     * @param status the match status
     * @return a list of matches with the given status
     */
    List<Match> findByStatus(String status);
    
    /**
     * Find matches scheduled between the given start and end times.
     *
     * @param startTime the start time
     * @param endTime the end time
     * @return a list of matches scheduled between the given times
     */
    List<Match> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * Find matches involving a specific team (either home or away).
     *
     * @param teamId the team ID
     * @return a list of matches involving the given team
     */
    List<Match> findByTeamId(UUID teamId);
    
    /**
     * Save a match.
     *
     * @param match the match to save
     * @return the saved match
     */
    Match save(Match match);
    
    /**
     * Delete a match.
     *
     * @param match the match to delete
     */
    void delete(Match match);
}
