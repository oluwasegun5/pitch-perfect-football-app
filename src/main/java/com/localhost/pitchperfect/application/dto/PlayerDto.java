package com.localhost.pitchperfect.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for Player entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDto {
    private UUID id;
    private String name;
    private LocalDate dateOfBirth;
    private String nationality;
    private String position;
    private String jerseyNumber;
    private String photoUrl;
    private int age;
}
