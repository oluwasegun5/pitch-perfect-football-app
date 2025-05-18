package com.localhost.pitchperfect.application.service;

import com.localhost.pitchperfect.application.dto.TeamDto;
import com.localhost.pitchperfect.application.dto.PlayerDto;
import com.localhost.pitchperfect.application.port.in.TeamUseCase;
import com.localhost.pitchperfect.application.port.out.TeamPersistencePort;
import com.localhost.pitchperfect.application.port.out.PlayerPersistencePort;
import com.localhost.pitchperfect.domain.model.Team;
import com.localhost.pitchperfect.domain.model.Player;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementation of the TeamUseCase interface.
 * This service implements the application logic for team-related use cases.
 */
@Service
@RequiredArgsConstructor
public class TeamService implements TeamUseCase {

    private final TeamPersistencePort teamPersistencePort;
    private final PlayerPersistencePort playerPersistencePort;
    private final MatchMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public TeamDto getTeamById(UUID teamId) {
        return teamPersistencePort.findById(teamId)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDto> getAllTeams() {
        return teamPersistencePort.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeamDto> getTeamsByCountry(String country) {
        return teamPersistencePort.findByCountry(country).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TeamDto createTeam(String name, String shortName, String country, String logoUrl) {
        Team team = new Team(name, shortName, country, logoUrl);
        Team savedTeam = teamPersistencePort.save(team);
        return mapper.toDto(savedTeam);
    }

    @Override
    @Transactional
    public TeamDto updateTeam(UUID teamId, String name, String shortName, String country, String logoUrl) {
        Team team = teamPersistencePort.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        
        team.updateInfo(name, shortName, country, logoUrl);
        Team savedTeam = teamPersistencePort.save(team);
        
        return mapper.toDto(savedTeam);
    }

    @Override
    @Transactional
    public TeamDto addPlayerToTeam(UUID teamId, UUID playerId) {
        Team team = teamPersistencePort.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        
        Player player = playerPersistencePort.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        team.addPlayer(player);
        Team savedTeam = teamPersistencePort.save(team);
        
        return mapper.toDto(savedTeam);
    }

    @Override
    @Transactional
    public TeamDto removePlayerFromTeam(UUID teamId, UUID playerId) {
        Team team = teamPersistencePort.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        
        Player player = playerPersistencePort.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));
        
        team.removePlayer(player);
        Team savedTeam = teamPersistencePort.save(team);
        
        return mapper.toDto(savedTeam);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDto> getTeamPlayers(UUID teamId) {
        Team team = teamPersistencePort.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team not found with ID: " + teamId));
        
        return team.getPlayers().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
