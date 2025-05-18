package com.localhost.pitchperfect.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for Match entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private UUID id;
    private TeamDto homeTeam;
    private TeamDto awayTeam;
    private String venue;
    private LocalDateTime startTime;
    private String status;
    private int homeScore;
    private int awayScore;
    private List<MatchEventDto> events;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
