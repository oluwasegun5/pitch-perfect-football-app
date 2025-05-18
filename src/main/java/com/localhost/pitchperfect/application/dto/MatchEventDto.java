package com.localhost.pitchperfect.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for MatchEvent entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchEventDto {
    private UUID id;
    private String type;
    private String description;
    private PlayerDto primaryPlayer;
    private PlayerDto secondaryPlayer;
    private LocalDateTime timestamp;
    private int matchMinute;
}
