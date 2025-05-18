package com.localhost.pitchperfect.infrastructure.web;

import com.localhost.pitchperfect.application.dto.PlayerDto;
import com.localhost.pitchperfect.application.port.in.PlayerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for player-related endpoints.
 * This adapter connects the application to the web layer.
 */
@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@Tag(name = "Player Management", description = "APIs for managing football players")
public class PlayerController {

    private final PlayerUseCase playerUseCase;

    @Operation(summary = "Get player by ID", description = "Retrieves a player by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player found", 
                    content = @Content(schema = @Schema(implementation = PlayerDto.class))),
        @ApiResponse(responseCode = "404", description = "Player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(
            @Parameter(description = "Player ID", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(playerUseCase.getPlayerById(id));
    }

    @Operation(summary = "Get all players", description = "Retrieves a list of all players")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        return ResponseEntity.ok(playerUseCase.getAllPlayers());
    }

    @Operation(summary = "Get players by team", description = "Retrieves players belonging to a specific team")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerDto>> getPlayersByTeam(
            @Parameter(description = "Team ID", required = true) @PathVariable UUID teamId) {
        return ResponseEntity.ok(playerUseCase.getPlayersByTeam(teamId));
    }

    @Operation(summary = "Get players by position", description = "Retrieves players filtered by their playing position")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/position/{position}")
    public ResponseEntity<List<PlayerDto>> getPlayersByPosition(
            @Parameter(description = "Player position (e.g., GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD)", required = true) 
            @PathVariable String position) {
        return ResponseEntity.ok(playerUseCase.getPlayersByPosition(position));
    }

    @Operation(summary = "Get players by nationality", description = "Retrieves players filtered by their nationality")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<PlayerDto>> getPlayersByNationality(
            @Parameter(description = "Player nationality", required = true) @PathVariable String nationality) {
        return ResponseEntity.ok(playerUseCase.getPlayersByNationality(nationality));
    }

    @Operation(summary = "Create a new player", description = "Creates a new player with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(
            @Parameter(description = "Player name", required = true) @RequestParam String name,
            @Parameter(description = "Date of birth (ISO format: YYYY-MM-DD)", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @Parameter(description = "Player nationality", required = true) @RequestParam String nationality,
            @Parameter(description = "Playing position", required = true) @RequestParam String position,
            @Parameter(description = "Jersey number", required = true) @RequestParam String jerseyNumber) {
        return ResponseEntity.ok(playerUseCase.createPlayer(name, dateOfBirth, nationality, position, jerseyNumber));
    }

    @Operation(summary = "Update player details", description = "Updates an existing player's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player updated successfully"),
        @ApiResponse(responseCode = "404", description = "Player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> updatePlayer(
            @Parameter(description = "Player ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Player name") @RequestParam(required = false) String name,
            @Parameter(description = "Date of birth (ISO format: YYYY-MM-DD)") 
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @Parameter(description = "Player nationality") @RequestParam(required = false) String nationality,
            @Parameter(description = "Playing position") @RequestParam(required = false) String position,
            @Parameter(description = "Jersey number") @RequestParam(required = false) String jerseyNumber) {
        return ResponseEntity.ok(playerUseCase.updatePlayer(id, name, dateOfBirth, nationality, position, jerseyNumber));
    }

    @Operation(summary = "Set player photo", description = "Updates a player's photo URL")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Photo updated successfully"),
        @ApiResponse(responseCode = "404", description = "Player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/photo")
    public ResponseEntity<PlayerDto> setPlayerPhoto(
            @Parameter(description = "Player ID", required = true) @PathVariable UUID id,
            @Parameter(description = "URL to player's photo", required = true) @RequestParam String photoUrl) {
        return ResponseEntity.ok(playerUseCase.setPlayerPhoto(id, photoUrl));
    }
}
