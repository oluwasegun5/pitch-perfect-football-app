package com.localhost.pitchperfect.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for MatchEvent.
 */
@Entity
@Table(name = "match_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventEntity {
    
    @Id
    private UUID id;
    
    @Column(nullable = false)
    private String type;
    
    @Column(nullable = false)
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "primary_player_id")
    private PlayerEntity primaryPlayer;
    
    @ManyToOne
    @JoinColumn(name = "secondary_player_id")
    private PlayerEntity secondaryPlayer;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private int matchMinute;
}
