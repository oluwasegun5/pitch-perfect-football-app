package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.application.port.out.TeamPersistencePort;
import com.localhost.pitchperfect.domain.model.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the TeamPersistencePort.
 * This adapter connects the application to the database.
 */
@Component
@RequiredArgsConstructor
public class TeamPersistenceAdapter implements TeamPersistencePort {

    private final TeamJpaRepository teamRepository;
    private final MatchPersistenceMapper mapper;

    @Override
    public Optional<Team> findById(UUID id) {
        return teamRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Team> findAll() {
        return teamRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Team> findByCountry(String country) {
        return teamRepository.findByCountry(country).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Team> findByName(String name) {
        return teamRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public Team save(Team team) {
        var entity = mapper.toEntity(team);
        var savedEntity = teamRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Team team) {
        var entity = mapper.toEntity(team);
        teamRepository.delete(entity);
    }
}
