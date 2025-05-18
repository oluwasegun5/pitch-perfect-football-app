package com.localhost.pitchperfect.domain.repository;

import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.model.Position;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Player entity in the domain layer.
 * This is a port in the hexagonal architecture that will be implemented by adapters.
 */
public interface PlayerRepository {
    
    /**
     * Find a player by ID.
     *
     * @param id the player ID
     * @return an Optional containing the player if found, or empty if not found
     */
    Optional<Player> findById(UUID id);
    
    /**
     * Find all players.
     *
     * @return a list of all players
     */
    List<Player> findAll();
    
    /**
     * Find players by team ID.
     *
     * @param teamId the team ID
     * @return a list of players in the given team
     */
    List<Player> findByTeamId(UUID teamId);
    
    /**
     * Find players by position.
     *
     * @param position the player position
     * @return a list of players with the given position
     */
    List<Player> findByPosition(Position position);
    
    /**
     * Find players by nationality.
     *
     * @param nationality the player nationality
     * @return a list of players with the given nationality
     */
    List<Player> findByNationality(String nationality);
    
    /**
     * Save a player.
     *
     * @param player the player to save
     * @return the saved player
     */
    Player save(Player player);
    
    /**
     * Delete a player.
     *
     * @param player the player to delete
     */
    void delete(Player player);
}
