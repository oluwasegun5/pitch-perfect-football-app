package com.localhost.pitchperfect.infrastructure.web;

import com.localhost.pitchperfect.application.dto.TeamDto;
import com.localhost.pitchperfect.application.dto.PlayerDto;
import com.localhost.pitchperfect.application.port.in.TeamUseCase;
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
public class TeamController {

    private final TeamUseCase teamUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getTeamById(@PathVariable UUID id) {
        return ResponseEntity.ok(teamUseCase.getTeamById(id));
    }

    @GetMapping
    public ResponseEntity<List<TeamDto>> getAllTeams() {
        return ResponseEntity.ok(teamUseCase.getAllTeams());
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<TeamDto>> getTeamsByCountry(@PathVariable String country) {
        return ResponseEntity.ok(teamUseCase.getTeamsByCountry(country));
    }

    @PostMapping
    public ResponseEntity<TeamDto> createTeam(
            @RequestParam String name,
            @RequestParam String shortName,
            @RequestParam String country,
            @RequestParam(required = false) String logoUrl) {
        return ResponseEntity.ok(teamUseCase.createTeam(name, shortName, country, logoUrl));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> updateTeam(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String shortName,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String logoUrl) {
        return ResponseEntity.ok(teamUseCase.updateTeam(id, name, shortName, country, logoUrl));
    }

    @PostMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDto> addPlayerToTeam(
            @PathVariable UUID teamId,
            @PathVariable UUID playerId) {
        return ResponseEntity.ok(teamUseCase.addPlayerToTeam(teamId, playerId));
    }

    @DeleteMapping("/{teamId}/players/{playerId}")
    public ResponseEntity<TeamDto> removePlayerFromTeam(
            @PathVariable UUID teamId,
            @PathVariable UUID playerId) {
        return ResponseEntity.ok(teamUseCase.removePlayerFromTeam(teamId, playerId));
    }

    @GetMapping("/{teamId}/players")
    public ResponseEntity<List<PlayerDto>> getTeamPlayers(@PathVariable UUID teamId) {
        return ResponseEntity.ok(teamUseCase.getTeamPlayers(teamId));
    }
}
