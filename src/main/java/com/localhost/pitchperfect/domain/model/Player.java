package com.localhost.pitchperfect.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Player entity representing a football player in the domain layer.
 * Core domain entity with business logic and invariants.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Player {
    private UUID id;
    private String name;
    private LocalDate dateOfBirth;
    private String nationality;
    private Position position;
    private String jerseyNumber;
    private String photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Player(String name, LocalDate dateOfBirth, String nationality, Position position, String jerseyNumber) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        validateName();
        validateDateOfBirth();
        validateJerseyNumber();
    }

    /**
     * Update player information.
     */
    public void updateInfo(String name, LocalDate dateOfBirth, String nationality, Position position, String jerseyNumber) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            validateName();
        }
        
        if (dateOfBirth != null && !dateOfBirth.equals(this.dateOfBirth)) {
            this.dateOfBirth = dateOfBirth;
            validateDateOfBirth();
        }
        
        if (nationality != null) {
            this.nationality = nationality;
        }
        
        if (position != null) {
            this.position = position;
        }
        
        if (jerseyNumber != null && !jerseyNumber.equals(this.jerseyNumber)) {
            this.jerseyNumber = jerseyNumber;
            validateJerseyNumber();
        }
        
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Set player photo URL.
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Calculate player's age.
     */
    public int getAge() {
        if (dateOfBirth == null) {
            return 0;
        }
        
        LocalDate now = LocalDate.now();
        int age = now.getYear() - dateOfBirth.getYear();
        
        // Adjust age if birthday hasn't occurred yet this year
        if (now.getMonthValue() < dateOfBirth.getMonthValue() || 
            (now.getMonthValue() == dateOfBirth.getMonthValue() && now.getDayOfMonth() < dateOfBirth.getDayOfMonth())) {
            age--;
        }
        
        return age;
    }

    /**
     * Validate player name.
     */
    private void validateName() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        
        if (name.length() < 2) {
            throw new IllegalArgumentException("Player name must be at least 2 characters long");
        }
        
        if (name.length() > 100) {
            throw new IllegalArgumentException("Player name cannot exceed 100 characters");
        }
    }

    /**
     * Validate player date of birth.
     */
    private void validateDateOfBirth() {
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        
        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of birth cannot be in the future");
        }
        
        // Check if player is at least 16 years old
        if (getAge() < 16) {
            throw new IllegalArgumentException("Player must be at least 16 years old");
        }
    }

    /**
     * Validate jersey number.
     */
    private void validateJerseyNumber() {
        if (jerseyNumber == null || jerseyNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Jersey number cannot be empty");
        }
        
        try {
            int number = Integer.parseInt(jerseyNumber);
            if (number < 1 || number > 99) {
                throw new IllegalArgumentException("Jersey number must be between 1 and 99");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Jersey number must be a valid number");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
