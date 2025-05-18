package com.localhost.pitchperfect.domain.repository;

import com.localhost.pitchperfect.domain.model.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Team entity in the domain layer.
 * This is a port in the hexagonal architecture that will be implemented by adapters.
 */
public interface TeamRepository {
    
    /**
     * Find a team by its ID.
     *
     * @param id the team ID
     * @return an Optional containing the team if found, or empty if not found
     */
    Optional<Team> findById(UUID id);
    
    /**
     * Find all teams.
     *
     * @return a list of all teams
     */
    List<Team> findAll();
    
    /**
     * Find teams by country.
     *
     * @param country the country
     * @return a list of teams from the given country
     */
    List<Team> findByCountry(String country);
    
    /**
     * Find a team by its name.
     *
     * @param name the team name
     * @return an Optional containing the team if found, or empty if not found
     */
    Optional<Team> findByName(String name);
    
    /**
     * Save a team.
     *
     * @param team the team to save
     * @return the saved team
     */
    Team save(Team team);
    
    /**
     * Delete a team.
     *
     * @param team the team to delete
     */
    void delete(Team team);
}
