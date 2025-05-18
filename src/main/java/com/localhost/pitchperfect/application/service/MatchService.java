package com.localhost.pitchperfect.application.service;

import com.localhost.pitchperfect.application.dto.MatchDto;
import com.localhost.pitchperfect.application.dto.MatchEventDto;
import com.localhost.pitchperfect.application.port.in.MatchUseCase;
import com.localhost.pitchperfect.application.port.out.MatchPersistencePort;
import com.localhost.pitchperfect.application.port.out.PlayerPersistencePort;
import com.localhost.pitchperfect.application.port.out.TeamPersistencePort;
import com.localhost.pitchperfect.domain.model.Match;
import com.localhost.pitchperfect.domain.model.MatchEvent;
import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.model.Team;
import com.localhost.pitchperfect.domain.service.MatchDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the MatchUseCase interface.
 * This service implements the application logic for match-related use cases.
 */
@Service
@RequiredArgsConstructor
public class MatchService implements MatchUseCase {

    private final MatchPersistencePort matchPersistencePort;
    private final TeamPersistencePort teamPersistencePort;
    private final PlayerPersistencePort playerPersistencePort;
    private final MatchDomainService matchDomainService;
    private final MatchMapper matchMapper;

    @Override
    @Transactional(readOnly = true)
    public MatchDto getMatchById(UUID matchId) {
        return matchPersistencePort.findById(matchId)
                .map(matchMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchDto> getAllMatches() {
        return matchPersistencePort.findAll().stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchDto> getMatchesByStatus(String status) {
        return matchPersistencePort.findByStatus(status).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchDto> getMatchesByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return matchPersistencePort.findByStartTimeBetween(startTime, endTime).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchDto> getMatchesByTeam(UUID teamId) {
        return matchPersistencePort.findByTeamId(teamId).stream()
                .map(matchMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MatchDto createMatch(UUID homeTeamId, UUID awayTeamId, String venue, LocalDateTime startTime) {
        Team homeTeam = teamPersistencePort.findById(homeTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Home team not found with ID: " + homeTeamId));
        
        Team awayTeam = teamPersistencePort.findById(awayTeamId)
                .orElseThrow(() -> new IllegalArgumentException("Away team not found with ID: " + awayTeamId));
        
        Match match = new Match(homeTeam, awayTeam, venue, startTime);
        Match savedMatch = matchPersistencePort.save(match);
        
        return matchMapper.toDto(savedMatch);
    }

    @Override
    @Transactional
    public MatchDto startMatch(UUID matchId) {
        Match match = matchPersistencePort.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        Match updatedMatch = matchDomainService.startMatch(match);
        Match savedMatch = matchPersistencePort.save(updatedMatch);
        
        return matchMapper.toDto(savedMatch);
    }

    @Override
    @Transactional
    public MatchDto completeMatch(UUID matchId) {
        Match match = matchPersistencePort.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        Match updatedMatch = matchDomainService.completeMatch(match);
        Match savedMatch = matchPersistencePort.save(updatedMatch);
        
        return matchMapper.toDto(savedMatch);
    }

    @Override
    @Transactional
    public MatchDto cancelMatch(UUID matchId, String reason) {
        Match match = matchPersistencePort.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        Match updatedMatch = matchDomainService.cancelMatch(match, reason);
        Match savedMatch = matchPersistencePort.save(updatedMatch);
        
        return matchMapper.toDto(savedMatch);
    }

    @Override
    @Transactional
    public MatchDto updateScore(UUID matchId, int homeScore, int awayScore) {
        Match match = matchPersistencePort.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        Match updatedMatch = matchDomainService.updateScore(match, homeScore, awayScore);
        Match savedMatch = matchPersistencePort.save(updatedMatch);
        
        return matchMapper.toDto(savedMatch);
    }

    @Override
    @Transactional
    public MatchDto addGoal(UUID matchId, UUID scorerId, UUID assistantId, boolean isHomeTeam) {
        Match match = matchPersistencePort.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        Player scorer = playerPersistencePort.findById(scorerId)
                .orElseThrow(() -> new IllegalArgumentException("Scorer not found with ID: " + scorerId));
        
        Player assistant = null;
        if (assistantId != null) {
            assistant = playerPersistencePort.findById(assistantId)
                    .orElseThrow(() -> new IllegalArgumentException("Assistant not found with ID: " + assistantId));
        }
        
        Match updatedMatch = matchDomainService.addGoal(match, scorer, assistant, isHomeTeam);
        Match savedMatch = matchPersistencePort.save(updatedMatch);
        
        return matchMapper.toDto(savedMatch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MatchEventDto> getMatchEvents(UUID matchId) {
        Match match = matchPersistencePort.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("Match not found with ID: " + matchId));
        
        return match.getEvents().stream()
                .map(matchMapper::toEventDto)
                .collect(Collectors.toList());
    }
}
