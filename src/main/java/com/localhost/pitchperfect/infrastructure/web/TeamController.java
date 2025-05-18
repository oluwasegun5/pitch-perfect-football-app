package com.localhost.pitchperfect.infrastructure.web;

import com.localhost.pitchperfect.application.dto.TeamDto;
import com.localhost.pitchperfect.application.dto.PlayerDto;
import com.localhost.pitchperfect.application.port.in.TeamUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for team-related endpoints.
 * This adapter connects the application to the web layer.
 */
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Team Management", description = "APIs for managing football teams")
public class TeamController {

    private final TeamUseCase teamUseCase;

    @Operation(summary = "Get team by ID", description = "Retrieves a team by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team found", 
                    content = @Content(schema = @Schema(implementation = TeamDto.class))),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getTeamById(
            @Parameter(description = "Team ID", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(teamUseCase.getTeamById(id));
    }

    @Operation(summary = "Get all teams", description = "Retrieves a list of all teams")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        return ResponseEntity.ok(teamUseCase.getAllTeams());
    }

    @Operation(summary = "Get teams by country", description = "Retrieves teams filtered by their country")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/country/{country}")
    public ResponseEntity<List<TeamDto>> getTeamsByCountry(
            @Parameter(description = "Country name", required = true) @PathVariable String country) {
        return ResponseEntity.ok(teamUseCase.getTeamsByCountry(country));
    }

    @Operation(summary = "Create a new team", description = "Creates a new team with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<TeamDto> createTeam(
            @Parameter(description = "Team name", required = true) @RequestParam String name,
            @Parameter(description = "Team short name or abbreviation", required = true) @RequestParam String shortName,
            @Parameter(description = "Team country", required = true) @RequestParam String country,
            @Parameter(description = "URL to team logo") @RequestParam(required = false) String logoUrl) {
        return ResponseEntity.ok(teamUseCase.createTeam(name, shortName, country, logoUrl));
    }

    @Operation(summary = "Update team details", description = "Updates an existing team's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Team updated successfully"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> updateTeam(
            @Parameter(description = "Team ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Team name") @RequestParam(required = false) String name,
            @Parameter(description = "Team short name or abbreviation") @RequestParam(required = false) String shortName,
            @Parameter(description = "Team country") @RequestParam(required = false) String country,
            @Parameter(description = "URL to team logo") @RequestParam(required = false) String logoUrl) {
        return ResponseEntity.ok(teamUseCase.updateTeam(id, name, shortName, country, logoUrl));
    }

    @Operation(summary = "Add player to team", description = "Adds a player to a team's roster")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player added successfully"),
        @ApiResponse(responseCode = "404", description = "Team or player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDto> addPlayerToTeam(
            @Parameter(description = "Team ID", required = true) @PathVariable UUID teamId,
            @Parameter(description = "Player ID", required = true) @PathVariable UUID playerId) {
        return ResponseEntity.ok(teamUseCase.addPlayerToTeam(teamId, playerId));
    }

    @Operation(summary = "Remove player from team", description = "Removes a player from a team's roster")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Player removed successfully"),
        @ApiResponse(responseCode = "404", description = "Team or player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDto> removePlayerFromTeam(
            @Parameter(description = "Team ID", required = true) @PathVariable UUID teamId,
            @Parameter(description = "Player ID", required = true) @PathVariable UUID playerId) {
        return ResponseEntity.ok(teamUseCase.removePlayerFromTeam(teamId, playerId));
    }

    @Operation(summary = "Get team players", description = "Retrieves all players in a team's roster")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Team not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{teamId}/players")
    public ResponseEntity<List<PlayerDto>> getTeamPlayers(
            @Parameter(description = "Team ID", required = true) @PathVariable UUID teamId) {
        return ResponseEntity.ok(teamUseCase.getTeamPlayers(teamId));
    }
}
