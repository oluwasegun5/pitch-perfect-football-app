package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.domain.model.Match;
import com.localhost.pitchperfect.domain.model.MatchEvent;
import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.model.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper for converting between domain entities and JPA entities.
 * Uses MapStruct for automatic mapping implementation.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchPersistenceMapper {
    
    @Mapping(target = "homeTeam", source = "homeTeam")
    @Mapping(target = "awayTeam", source = "awayTeam")
    @Mapping(target = "events", source = "events")
    Match toDomain(MatchEntity entity);
    
    @Mapping(target = "homeTeam", source = "homeTeam")
    @Mapping(target = "awayTeam", source = "awayTeam")
    @Mapping(target = "events", source = "events")
    MatchEntity toEntity(Match domain);
    
    MatchEvent toDomain(MatchEventEntity entity);
    
    MatchEventEntity toEntity(MatchEvent domain);
    
    Team toDomain(TeamEntity entity);
    
    TeamEntity toEntity(Team domain);
    
    Player toDomain(PlayerEntity entity);
    
    PlayerEntity toEntity(Player domain);
}
