package com.localhost.pitchperfect.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object for Team entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private UUID id;
    private String name;
    private String shortName;
    private String country;
    private String logoUrl;
    private List<PlayerDto> players;
}
