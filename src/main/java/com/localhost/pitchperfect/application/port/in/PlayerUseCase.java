package com.localhost.pitchperfect.application.port.in;

import com.localhost.pitchperfect.application.dto.PlayerDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Input port for player-related use cases.
 * This is a port in the hexagonal architecture that defines the API for the application layer.
 */
public interface PlayerUseCase {
    
    /**
     * Get a player by ID.
     *
     * @param playerId the player ID
     * @return the player DTO
     */
    PlayerDto getPlayerById(UUID playerId);
    
    /**
     * Get all players.
     *
     * @return a list of all players
     */
    List<PlayerDto> getAllPlayers();
    
    /**
     * Get players by team.
     *
     * @param teamId the team ID
     * @return a list of players in the given team
     */
    List<PlayerDto> getPlayersByTeam(UUID teamId);
    
    /**
     * Get players by position.
     *
     * @param position the player position
     * @return a list of players with the given position
     */
    List<PlayerDto> getPlayersByPosition(String position);
    
    /**
     * Get players by nationality.
     *
     * @param nationality the player nationality
     * @return a list of players with the given nationality
     */
    List<PlayerDto> getPlayersByNationality(String nationality);
    
    /**
     * Create a new player.
     *
     * @param name the player name
     * @param dateOfBirth the player date of birth
     * @param nationality the player nationality
     * @param position the player position
     * @param jerseyNumber the player jersey number
     * @return the created player DTO
     */
    PlayerDto createPlayer(String name, LocalDate dateOfBirth, String nationality, String position, String jerseyNumber);
    
    /**
     * Update player information.
     *
     * @param playerId the player ID
     * @param name the player name
     * @param dateOfBirth the player date of birth
     * @param nationality the player nationality
     * @param position the player position
     * @param jerseyNumber the player jersey number
     * @return the updated player DTO
     */
    PlayerDto updatePlayer(UUID playerId, String name, LocalDate dateOfBirth, String nationality, String position, String jerseyNumber);
    
    /**
     * Set player photo URL.
     *
     * @param playerId the player ID
     * @param photoUrl the player photo URL
     * @return the updated player DTO
     */
    PlayerDto setPlayerPhoto(UUID playerId, String photoUrl);
}
