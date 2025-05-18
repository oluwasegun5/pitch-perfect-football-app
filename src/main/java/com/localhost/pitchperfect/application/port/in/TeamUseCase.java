package com.localhost.pitchperfect.application.port.in;

import com.localhost.pitchperfect.application.dto.TeamDto;
import com.localhost.pitchperfect.application.dto.PlayerDto;

import java.util.List;
import java.util.UUID;

/**
 * Input port for team-related use cases.
 * This is a port in the hexagonal architecture that defines the API for the application layer.
 */
public interface TeamUseCase {
    
    /**
     * Get a team by ID.
     *
     * @param teamId the team ID
     * @return the team DTO
     */
    TeamDto getTeamById(UUID teamId);
    
    /**
     * Get all teams.
     *
     * @return a list of all teams
     */
    List<TeamDto> getAllTeams();
    
    /**
     * Get teams by country.
     *
     * @param country the country
     * @return a list of teams from the given country
     */
    List<TeamDto> getTeamsByCountry(String country);
    
    /**
     * Create a new team.
     *
     * @param name the team name
     * @param shortName the team short name
     * @param country the team country
     * @param logoUrl the team logo URL
     * @return the created team DTO
     */
    TeamDto createTeam(String name, String shortName, String country, String logoUrl);
    
    /**
     * Update team information.
     *
     * @param teamId the team ID
     * @param name the team name
     * @param shortName the team short name
     * @param country the team country
     * @param logoUrl the team logo URL
     * @return the updated team DTO
     */
    TeamDto updateTeam(UUID teamId, String name, String shortName, String country, String logoUrl);
    
    /**
     * Add a player to a team.
     *
     * @param teamId the team ID
     * @param playerId the player ID
     * @return the updated team DTO
     */
    TeamDto addPlayerToTeam(UUID teamId, UUID playerId);
    
    /**
     * Remove a player from a team.
     *
     * @param teamId the team ID
     * @param playerId the player ID
     * @return the updated team DTO
     */
    TeamDto removePlayerFromTeam(UUID teamId, UUID playerId);
    
    /**
     * Get players in a team.
     *
     * @param teamId the team ID
     * @return a list of player DTOs
     */
    List<PlayerDto> getTeamPlayers(UUID teamId);
}
