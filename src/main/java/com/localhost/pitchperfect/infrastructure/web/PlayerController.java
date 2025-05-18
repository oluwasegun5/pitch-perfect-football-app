package com.localhost.pitchperfect.infrastructure.web;

import com.localhost.pitchperfect.application.dto.PlayerDto;
import com.localhost.pitchperfect.application.port.in.PlayerUseCase;
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
public class PlayerController {

    private final PlayerUseCase playerUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayerById(@PathVariable UUID id) {
        return ResponseEntity.ok(playerUseCase.getPlayerById(id));
    }

    @GetMapping
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        return ResponseEntity.ok(playerUseCase.getAllPlayers());
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<PlayerDto>> getPlayersByTeam(@PathVariable UUID teamId) {
        return ResponseEntity.ok(playerUseCase.getPlayersByTeam(teamId));
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<PlayerDto>> getPlayersByPosition(@PathVariable String position) {
        return ResponseEntity.ok(playerUseCase.getPlayersByPosition(position));
    }

    @GetMapping("/nationality/{nationality}")
    public ResponseEntity<List<PlayerDto>> getPlayersByNationality(@PathVariable String nationality) {
        return ResponseEntity.ok(playerUseCase.getPlayersByNationality(nationality));
    }

    @PostMapping
    public ResponseEntity<PlayerDto> createPlayer(
            @RequestParam String name,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam String nationality,
            @RequestParam String position,
            @RequestParam String jerseyNumber) {
        return ResponseEntity.ok(playerUseCase.createPlayer(name, dateOfBirth, nationality, position, jerseyNumber));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDto> updatePlayer(
            @PathVariable UUID id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateOfBirth,
            @RequestParam(required = false) String nationality,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String jerseyNumber) {
        return ResponseEntity.ok(playerUseCase.updatePlayer(id, name, dateOfBirth, nationality, position, jerseyNumber));
    }

    @PutMapping("/{id}/photo")
    public ResponseEntity<PlayerDto> setPlayerPhoto(
            @PathVariable UUID id,
            @RequestParam String photoUrl) {
        return ResponseEntity.ok(playerUseCase.setPlayerPhoto(id, photoUrl));
    }
}
