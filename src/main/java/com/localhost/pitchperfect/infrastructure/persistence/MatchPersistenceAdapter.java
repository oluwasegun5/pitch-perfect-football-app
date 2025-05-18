package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.application.port.out.MatchPersistencePort;
import com.localhost.pitchperfect.domain.model.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the MatchPersistencePort.
 * This adapter connects the application to the database.
 */
@Component
@RequiredArgsConstructor
public class MatchPersistenceAdapter implements MatchPersistencePort {

    private final MatchJpaRepository matchRepository;
    private final MatchPersistenceMapper mapper;

    @Override
    public Optional<Match> findById(UUID id) {
        return matchRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Match> findAll() {
        return matchRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Match> findByStatus(String status) {
        return matchRepository.findByStatus(status).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Match> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return matchRepository.findByStartTimeBetween(startTime, endTime).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Match> findByTeamId(UUID teamId) {
        return matchRepository.findByHomeTeamIdOrAwayTeamId(teamId, teamId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Match save(Match match) {
        var entity = mapper.toEntity(match);
        var savedEntity = matchRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Match match) {
        var entity = mapper.toEntity(match);
        matchRepository.delete(entity);
    }
}
