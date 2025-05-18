package com.localhost.pitchperfect.application.service;

import com.localhost.pitchperfect.application.dto.PlayerDto;
import com.localhost.pitchperfect.application.port.in.PlayerUseCase;
import com.localhost.pitchperfect.application.port.out.PlayerPersistencePort;
import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.model.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the PlayerUseCase interface.
 * This service implements the application logic for player-related use cases.
 */
@Service
@RequiredArgsConstructor
public class PlayerService implements PlayerUseCase {

    private final PlayerPersistencePort playerPersistencePort;
    private final MatchMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public PlayerDto getPlayerById(UUID playerId) {
        return playerPersistencePort.findById(playerId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> getAllPlayers() {
        return playerPersistencePort.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> getPlayersByTeam(UUID teamId) {
        return playerPersistencePort.findByTeamId(teamId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> getPlayersByPosition(String position) {
        Position positionEnum = Position.valueOf(position);
        return playerPersistencePort.findByPosition(positionEnum).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> getPlayersByNationality(String nationality) {
        return playerPersistencePort.findByNationality(nationality).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PlayerDto createPlayer(String name, LocalDate dateOfBirth, String nationality, String position, String jerseyNumber) {
        Position positionEnum = Position.valueOf(position);
        Player player = new Player(name, dateOfBirth, nationality, positionEnum, jerseyNumber);
        Player savedPlayer = playerPersistencePort.save(player);
        return mapper.toDto(savedPlayer);
    }

    @Override
    @Transactional
    public PlayerDto updatePlayer(UUID playerId, String name, LocalDate dateOfBirth, String nationality, String position, String jerseyNumber) {
        Player player = playerPersistencePort.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        Position positionEnum = position != null ? Position.valueOf(position) : null;
        player.updateInfo(name, dateOfBirth, nationality, positionEnum, jerseyNumber);
        Player savedPlayer = playerPersistencePort.save(player);
        
        return mapper.toDto(savedPlayer);
    }

    @Override
    @Transactional
    public PlayerDto setPlayerPhoto(UUID playerId, String photoUrl) {
        Player player = playerPersistencePort.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        player.setPhotoUrl(photoUrl);
        Player savedPlayer = playerPersistencePort.save(player);
        
        return mapper.toDto(savedPlayer);
    }
}
