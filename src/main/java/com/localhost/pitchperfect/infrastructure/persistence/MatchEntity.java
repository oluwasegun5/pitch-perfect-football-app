package com.localhost.pitchperfect.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for Match.
 */
@Entity
@Table(name = "matches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEntity {
    
    @Id
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "home_team_id", nullable = false)
    private TeamEntity homeTeam;
    
    @ManyToOne
    @JoinColumn(name = "away_team_id", nullable = false)
    private TeamEntity awayTeam;
    
    @Column(nullable = false)
    private String venue;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private int homeScore;
    
    @Column(nullable = false)
    private int awayScore;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "match_id")
    private List<MatchEventEntity> events = new ArrayList<>();
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
