package com.localhost.pitchperfect.infrastructure.web;

import com.localhost.pitchperfect.application.dto.MatchDto;
import com.localhost.pitchperfect.application.dto.MatchEventDto;
import com.localhost.pitchperfect.application.port.in.MatchUseCase;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * REST Controller for match-related endpoints.
 * This adapter connects the application to the web layer.
 */
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Match Management", description = "APIs for managing football matches")
public class MatchController {

    private final MatchUseCase matchUseCase;

    @Operation(summary = "Get match by ID", description = "Retrieves a match by its unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match found", 
                    content = @Content(schema = @Schema(implementation = MatchDto.class))),
        @ApiResponse(responseCode = "404", description = "Match not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getMatchById(
            @Parameter(description = "Match ID", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.getMatchById(id));
    }

    @Operation(summary = "Get all matches", description = "Retrieves a list of all matches")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        return ResponseEntity.ok(matchUseCase.getAllMatches());
    }

    @Operation(summary = "Get matches by status", description = "Retrieves matches filtered by their status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MatchDto>> getMatchesByStatus(
            @Parameter(description = "Match status (e.g., SCHEDULED, LIVE, COMPLETED)", required = true) 
            @PathVariable String status) {
        return ResponseEntity.ok(matchUseCase.getMatchesByStatus(status));
    }

    @Operation(summary = "Get matches by time range", description = "Retrieves matches scheduled within a specific time range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "400", description = "Invalid time range parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/timerange")
    public ResponseEntity<List<MatchDto>> getMatchesByTimeRange(
            @Parameter(description = "Start time (ISO format)", required = true) @RequestParam LocalDateTime startTime,
            @Parameter(description = "End time (ISO format)", required = true) @RequestParam LocalDateTime endTime) {
        return ResponseEntity.ok(matchUseCase.getMatchesByTimeRange(startTime, endTime));
    }

    @Operation(summary = "Get matches by team", description = "Retrieves matches involving a specific team")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<MatchDto>> getMatchesByTeam(
            @Parameter(description = "Team ID", required = true) @PathVariable UUID teamId) {
        return ResponseEntity.ok(matchUseCase.getMatchesByTeam(teamId));
    }

    @Operation(summary = "Create a new match", description = "Creates a new match with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<MatchDto> createMatch(
            @Parameter(description = "Home team ID", required = true) @RequestParam UUID homeTeamId,
            @Parameter(description = "Away team ID", required = true) @RequestParam UUID awayTeamId,
            @Parameter(description = "Match venue", required = true) @RequestParam String venue,
            @Parameter(description = "Match start time (ISO format)", required = true) @RequestParam LocalDateTime startTime) {
        return ResponseEntity.ok(matchUseCase.createMatch(homeTeamId, awayTeamId, venue, startTime));
    }

    @Operation(summary = "Start a match", description = "Marks a match as started")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match started successfully"),
        @ApiResponse(responseCode = "404", description = "Match not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/start")
    public ResponseEntity<MatchDto> startMatch(
            @Parameter(description = "Match ID", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.startMatch(id));
    }

    @Operation(summary = "Complete a match", description = "Marks a match as completed")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match completed successfully"),
        @ApiResponse(responseCode = "404", description = "Match not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/complete")
    public ResponseEntity<MatchDto> completeMatch(
            @Parameter(description = "Match ID", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.completeMatch(id));
    }

    @Operation(summary = "Cancel a match", description = "Cancels a match with the provided reason")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Match cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Match not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/cancel")
    public ResponseEntity<MatchDto> cancelMatch(
            @Parameter(description = "Match ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Cancellation reason", required = true) @RequestParam String reason) {
        return ResponseEntity.ok(matchUseCase.cancelMatch(id, reason));
    }

    @Operation(summary = "Update match score", description = "Updates the score for a match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Score updated successfully"),
        @ApiResponse(responseCode = "404", description = "Match not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}/score")
    public ResponseEntity<MatchDto> updateScore(
            @Parameter(description = "Match ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Home team score", required = true) @RequestParam int homeScore,
            @Parameter(description = "Away team score", required = true) @RequestParam int awayScore) {
        return ResponseEntity.ok(matchUseCase.updateScore(id, homeScore, awayScore));
    }

    @Operation(summary = "Add a goal to a match", description = "Records a goal in a match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Goal added successfully"),
        @ApiResponse(responseCode = "404", description = "Match or player not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/{id}/goal")
    public ResponseEntity<MatchDto> addGoal(
            @Parameter(description = "Match ID", required = true) @PathVariable UUID id,
            @Parameter(description = "Scorer player ID", required = true) @RequestParam UUID scorerId,
            @Parameter(description = "Assistant player ID (optional)") @RequestParam(required = false) UUID assistantId,
            @Parameter(description = "Whether the goal is for the home team", required = true) @RequestParam boolean isHomeTeam) {
        return ResponseEntity.ok(matchUseCase.addGoal(id, scorerId, assistantId, isHomeTeam));
    }

    @Operation(summary = "Get match events", description = "Retrieves all events for a specific match")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "404", description = "Match not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}/events")
    public ResponseEntity<List<MatchEventDto>> getMatchEvents(
            @Parameter(description = "Match ID", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.getMatchEvents(id));
    }
}
