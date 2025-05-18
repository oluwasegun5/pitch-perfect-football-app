package com.localhost.pitchperfect.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * JPA Entity for Player.
 */
@Entity
@Table(name = "players")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEntity {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    @Column(nullable = false)
    private String nationality;
    
    @Column(nullable = false)
    private String position;
    
    @Column(nullable = false, length = 2)
    private String jerseyNumber;
    
    private String photoUrl;
    
    @ManyToOne
    @JoinColumn(name = "team_id")
    private TeamEntity team;
}
