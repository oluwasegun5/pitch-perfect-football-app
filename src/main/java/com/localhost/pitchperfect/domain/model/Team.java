package com.localhost.pitchperfect.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Team entity representing a football team in the domain layer.
 * Core domain entity with business logic and invariants.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    private UUID id;
    private String name;
    private String shortName;
    private String country;
    private String logoUrl;
    private List<Player> players;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Team(String name, String shortName, String country, String logoUrl) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.shortName = shortName;
        this.country = country;
        this.logoUrl = logoUrl;
        this.players = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        validateName();
        validateShortName();
    }

    /**
     * Add a player to the team.
     */
    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        
        if (players == null) {
            players = new ArrayList<>();
        }
        
        if (players.contains(player)) {
            throw new IllegalArgumentException("Player is already in the team");
        }
        
        players.add(player);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Remove a player from the team.
     */
    public void removePlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        
        if (players == null || !players.contains(player)) {
            throw new IllegalArgumentException("Player is not in the team");
        }
        
        players.remove(player);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Update team information.
     */
    public void updateInfo(String name, String shortName, String country, String logoUrl) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            validateName();
        }
        
        if (shortName != null && !shortName.equals(this.shortName)) {
            this.shortName = shortName;
            validateShortName();
        }
        
        if (country != null) {
            this.country = country;
        }
        
        if (logoUrl != null) {
            this.logoUrl = logoUrl;
        }
        
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Get the number of players in the team.
     */
    public int getSquadSize() {
        return players == null ? 0 : players.size();
    }

    /**
     * Check if the team has a specific player.
     */
    public boolean hasPlayer(Player player) {
        return players != null && players.contains(player);
    }

    /**
     * Check if the team has a player with the given ID.
     */
    public boolean hasPlayerWithId(UUID playerId) {
        if (players == null || playerId == null) {
            return false;
        }
        
        return players.stream()
                .anyMatch(player -> player.getId().equals(playerId));
    }

    /**
     * Validate team name.
     */
    private void validateName() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Team name cannot be empty");
        }
        
        if (name.length() < 3) {
            throw new IllegalArgumentException("Team name must be at least 3 characters long");
        }
        
        if (name.length() > 100) {
            throw new IllegalArgumentException("Team name cannot exceed 100 characters");
        }
    }

    /**
     * Validate team short name.
     */
    private void validateShortName() {
        if (shortName == null || shortName.trim().isEmpty()) {
            throw new IllegalArgumentException("Team short name cannot be empty");
        }
        
        if (shortName.length() < 2) {
            throw new IllegalArgumentException("Team short name must be at least 2 characters long");
        }
        
        if (shortName.length() > 3) {
            throw new IllegalArgumentException("Team short name cannot exceed 3 characters");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
