package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.application.port.out.PlayerPersistencePort;
import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.model.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the PlayerPersistencePort.
 * This adapter connects the application to the database.
 */
@Component
@RequiredArgsConstructor
public class PlayerPersistenceAdapter implements PlayerPersistencePort {

    private final PlayerJpaRepository playerRepository;
    private final MatchPersistenceMapper mapper;

    @Override
    public Optional<Player> findById(UUID id) {
        return playerRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Player> findAll() {
        return playerRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Player> findByTeamId(UUID teamId) {
        return playerRepository.findByTeamId(teamId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Player> findByPosition(Position position) {
        return playerRepository.findByPosition(position.name()).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<Player> findByNationality(String nationality) {
        return playerRepository.findByNationality(nationality).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Player save(Player player) {
        var entity = mapper.toEntity(player);
        var savedEntity = playerRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Player player) {
        var entity = mapper.toEntity(player);
        playerRepository.delete(entity);
    }
}
