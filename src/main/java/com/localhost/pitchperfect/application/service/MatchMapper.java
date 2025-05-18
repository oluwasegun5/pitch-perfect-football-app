package com.localhost.pitchperfect.application.service;

import com.localhost.pitchperfect.application.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.localhost.pitchperfect.domain.model.*;

/**
 * Mapper for converting between domain entities and DTOs.
 * Uses MapStruct for automatic mapping implementation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchMapper {
    
    @Mapping(target = "homeTeam", source = "homeTeam")
    @Mapping(target = "awayTeam", source = "awayTeam")
    @Mapping(target = "status", expression = "java(match.getStatus().name())")
    MatchDto toDto(Match match);
    
    @Mapping(target = "type", expression = "java(event.getType().name())")
    MatchEventDto toEventDto(MatchEvent event);
    
    TeamDto toDto(Team team);
    
    @Mapping(target = "position", expression = "java(player.getPosition().getDisplayName())")
    @Mapping(target = "age", expression = "java(player.getAge())")
    PlayerDto toDto(Player player);
}
