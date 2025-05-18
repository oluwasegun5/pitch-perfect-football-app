package com.localhost.pitchperfect.infrastructure.web;

import com.localhost.pitchperfect.application.dto.MatchDto;
import com.localhost.pitchperfect.application.dto.MatchEventDto;
import com.localhost.pitchperfect.application.port.in.MatchUseCase;
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
public class MatchController {

    private final MatchUseCase matchUseCase;

    @GetMapping("/{id}")
    public ResponseEntity<MatchDto> getMatchById(@PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.getMatchById(id));
    }

    @GetMapping
    public ResponseEntity<List<MatchDto>> getAllMatches() {
        return ResponseEntity.ok(matchUseCase.getAllMatches());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MatchDto>> getMatchesByStatus(@PathVariable String status) {
        return ResponseEntity.ok(matchUseCase.getMatchesByStatus(status));
    }

    @GetMapping("/timerange")
    public ResponseEntity<List<MatchDto>> getMatchesByTimeRange(
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        return ResponseEntity.ok(matchUseCase.getMatchesByTimeRange(startTime, endTime));
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<MatchDto>> getMatchesByTeam(@PathVariable UUID teamId) {
        return ResponseEntity.ok(matchUseCase.getMatchesByTeam(teamId));
    }

    @PostMapping
    public ResponseEntity<MatchDto> createMatch(
            @RequestParam UUID homeTeamId,
            @RequestParam UUID awayTeamId,
            @RequestParam String venue,
            @RequestParam LocalDateTime startTime) {
        return ResponseEntity.ok(matchUseCase.createMatch(homeTeamId, awayTeamId, venue, startTime));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<MatchDto> startMatch(@PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.startMatch(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<MatchDto> completeMatch(@PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.completeMatch(id));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<MatchDto> cancelMatch(
            @PathVariable UUID id,
            @RequestParam String reason) {
        return ResponseEntity.ok(matchUseCase.cancelMatch(id, reason));
    }

    @PutMapping("/{id}/score")
    public ResponseEntity<MatchDto> updateScore(
            @PathVariable UUID id,
            @RequestParam int homeScore,
            @RequestParam int awayScore) {
        return ResponseEntity.ok(matchUseCase.updateScore(id, homeScore, awayScore));
    }

    @PostMapping("/{id}/goal")
    public ResponseEntity<MatchDto> addGoal(
            @PathVariable UUID id,
            @RequestParam UUID scorerId,
            @RequestParam(required = false) UUID assistantId,
            @RequestParam boolean isHomeTeam) {
        return ResponseEntity.ok(matchUseCase.addGoal(id, scorerId, assistantId, isHomeTeam));
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<MatchEventDto>> getMatchEvents(@PathVariable UUID id) {
        return ResponseEntity.ok(matchUseCase.getMatchEvents(id));
    }
}
