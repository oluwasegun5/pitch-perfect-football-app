package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.domain.model.Match;
import com.localhost.pitchperfect.domain.model.MatchEvent;
import com.localhost.pitchperfect.domain.model.MatchEventType;
import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.model.Position;
import com.localhost.pitchperfect.domain.model.Team;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-18T16:06:55-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class MatchPersistenceMapperImpl implements MatchPersistenceMapper {

    @Override
    public Match toDomain(MatchEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Team homeTeam = null;
        Team awayTeam = null;
        String venue = null;
        LocalDateTime startTime = null;

        homeTeam = toDomain( entity.getHomeTeam() );
        awayTeam = toDomain( entity.getAwayTeam() );
        venue = entity.getVenue();
        startTime = entity.getStartTime();

        Match match = new Match( homeTeam, awayTeam, venue, startTime );

        if ( match.getEvents() != null ) {
            List<MatchEvent> list = matchEventEntityListToMatchEventList( entity.getEvents() );
            if ( list != null ) {
                match.getEvents().addAll( list );
            }
        }

        return match;
    }

    @Override
    public MatchEntity toEntity(Match domain) {
        if ( domain == null ) {
            return null;
        }

        MatchEntity.MatchEntityBuilder matchEntity = MatchEntity.builder();

        matchEntity.homeTeam( toEntity( domain.getHomeTeam() ) );
        matchEntity.awayTeam( toEntity( domain.getAwayTeam() ) );
        matchEntity.events( matchEventListToMatchEventEntityList( domain.getEvents() ) );
        matchEntity.id( domain.getId() );
        matchEntity.venue( domain.getVenue() );
        matchEntity.startTime( domain.getStartTime() );
        if ( domain.getStatus() != null ) {
            matchEntity.status( domain.getStatus().name() );
        }
        matchEntity.homeScore( domain.getHomeScore() );
        matchEntity.awayScore( domain.getAwayScore() );
        matchEntity.createdAt( domain.getCreatedAt() );
        matchEntity.updatedAt( domain.getUpdatedAt() );

        return matchEntity.build();
    }

    @Override
    public MatchEvent toDomain(MatchEventEntity entity) {
        if ( entity == null ) {
            return null;
        }

        MatchEvent matchEvent = new MatchEvent();

        matchEvent.setId( entity.getId() );
        if ( entity.getType() != null ) {
            matchEvent.setType( Enum.valueOf( MatchEventType.class, entity.getType() ) );
        }
        matchEvent.setDescription( entity.getDescription() );
        matchEvent.setPrimaryPlayer( toDomain( entity.getPrimaryPlayer() ) );
        matchEvent.setSecondaryPlayer( toDomain( entity.getSecondaryPlayer() ) );
        matchEvent.setTimestamp( entity.getTimestamp() );
        matchEvent.setMatchMinute( entity.getMatchMinute() );

        return matchEvent;
    }

    @Override
    public MatchEventEntity toEntity(MatchEvent domain) {
        if ( domain == null ) {
            return null;
        }

        MatchEventEntity.MatchEventEntityBuilder matchEventEntity = MatchEventEntity.builder();

        matchEventEntity.id( domain.getId() );
        if ( domain.getType() != null ) {
            matchEventEntity.type( domain.getType().name() );
        }
        matchEventEntity.description( domain.getDescription() );
        matchEventEntity.primaryPlayer( toEntity( domain.getPrimaryPlayer() ) );
        matchEventEntity.secondaryPlayer( toEntity( domain.getSecondaryPlayer() ) );
        matchEventEntity.timestamp( domain.getTimestamp() );
        matchEventEntity.matchMinute( domain.getMatchMinute() );

        return matchEventEntity.build();
    }

    @Override
    public Team toDomain(TeamEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String name = null;
        String shortName = null;
        String country = null;
        String logoUrl = null;

        name = entity.getName();
        shortName = entity.getShortName();
        country = entity.getCountry();
        logoUrl = entity.getLogoUrl();

        Team team = new Team( name, shortName, country, logoUrl );

        if ( team.getPlayers() != null ) {
            List<Player> list = playerEntityListToPlayerList( entity.getPlayers() );
            if ( list != null ) {
                team.getPlayers().addAll( list );
            }
        }

        return team;
    }

    @Override
    public TeamEntity toEntity(Team domain) {
        if ( domain == null ) {
            return null;
        }

        TeamEntity.TeamEntityBuilder teamEntity = TeamEntity.builder();

        teamEntity.id( domain.getId() );
        teamEntity.name( domain.getName() );
        teamEntity.shortName( domain.getShortName() );
        teamEntity.country( domain.getCountry() );
        teamEntity.logoUrl( domain.getLogoUrl() );
        teamEntity.players( playerListToPlayerEntityList( domain.getPlayers() ) );

        return teamEntity.build();
    }

    @Override
    public Player toDomain(PlayerEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String name = null;
        LocalDate dateOfBirth = null;
        String nationality = null;
        Position position = null;
        String jerseyNumber = null;

        name = entity.getName();
        dateOfBirth = entity.getDateOfBirth();
        nationality = entity.getNationality();
        if ( entity.getPosition() != null ) {
            position = Enum.valueOf( Position.class, entity.getPosition() );
        }
        jerseyNumber = entity.getJerseyNumber();

        Player player = new Player( name, dateOfBirth, nationality, position, jerseyNumber );

        player.setPhotoUrl( entity.getPhotoUrl() );

        return player;
    }

    @Override
    public PlayerEntity toEntity(Player domain) {
        if ( domain == null ) {
            return null;
        }

        PlayerEntity.PlayerEntityBuilder playerEntity = PlayerEntity.builder();

        playerEntity.id( domain.getId() );
        playerEntity.name( domain.getName() );
        playerEntity.dateOfBirth( domain.getDateOfBirth() );
        playerEntity.nationality( domain.getNationality() );
        if ( domain.getPosition() != null ) {
            playerEntity.position( domain.getPosition().name() );
        }
        playerEntity.jerseyNumber( domain.getJerseyNumber() );
        playerEntity.photoUrl( domain.getPhotoUrl() );

        return playerEntity.build();
    }

    protected List<MatchEvent> matchEventEntityListToMatchEventList(List<MatchEventEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<MatchEvent> list1 = new ArrayList<MatchEvent>( list.size() );
        for ( MatchEventEntity matchEventEntity : list ) {
            list1.add( toDomain( matchEventEntity ) );
        }

        return list1;
    }

    protected List<MatchEventEntity> matchEventListToMatchEventEntityList(List<MatchEvent> list) {
        if ( list == null ) {
            return null;
        }

        List<MatchEventEntity> list1 = new ArrayList<MatchEventEntity>( list.size() );
        for ( MatchEvent matchEvent : list ) {
            list1.add( toEntity( matchEvent ) );
        }

        return list1;
    }

    protected List<Player> playerEntityListToPlayerList(List<PlayerEntity> list) {
        if ( list == null ) {
            return null;
        }

        List<Player> list1 = new ArrayList<Player>( list.size() );
        for ( PlayerEntity playerEntity : list ) {
            list1.add( toDomain( playerEntity ) );
        }

        return list1;
    }

    protected List<PlayerEntity> playerListToPlayerEntityList(List<Player> list) {
        if ( list == null ) {
            return null;
        }

        List<PlayerEntity> list1 = new ArrayList<PlayerEntity>( list.size() );
        for ( Player player : list ) {
            list1.add( toEntity( player ) );
        }

        return list1;
    }
}
