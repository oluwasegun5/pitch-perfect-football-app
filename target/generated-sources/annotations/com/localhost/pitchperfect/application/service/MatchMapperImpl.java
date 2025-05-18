package com.localhost.pitchperfect.application.service;

import com.localhost.pitchperfect.application.dto.MatchDto;
import com.localhost.pitchperfect.application.dto.MatchEventDto;
import com.localhost.pitchperfect.application.dto.PlayerDto;
import com.localhost.pitchperfect.application.dto.TeamDto;
import com.localhost.pitchperfect.domain.model.Match;
import com.localhost.pitchperfect.domain.model.MatchEvent;
import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.model.Team;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-18T15:43:28-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class MatchMapperImpl implements MatchMapper {

    @Override
    public MatchDto toDto(Match match) {
        if ( match == null ) {
            return null;
        }

        MatchDto.MatchDtoBuilder matchDto = MatchDto.builder();

        matchDto.homeTeam( toDto( match.getHomeTeam() ) );
        matchDto.awayTeam( toDto( match.getAwayTeam() ) );
        matchDto.id( match.getId() );
        matchDto.venue( match.getVenue() );
        matchDto.startTime( match.getStartTime() );
        matchDto.homeScore( match.getHomeScore() );
        matchDto.awayScore( match.getAwayScore() );
        matchDto.events( matchEventListToMatchEventDtoList( match.getEvents() ) );
        matchDto.createdAt( match.getCreatedAt() );
        matchDto.updatedAt( match.getUpdatedAt() );

        matchDto.status( match.getStatus().name() );

        return matchDto.build();
    }

    @Override
    public MatchEventDto toEventDto(MatchEvent event) {
        if ( event == null ) {
            return null;
        }

        MatchEventDto.MatchEventDtoBuilder matchEventDto = MatchEventDto.builder();

        matchEventDto.id( event.getId() );
        matchEventDto.description( event.getDescription() );
        matchEventDto.primaryPlayer( toDto( event.getPrimaryPlayer() ) );
        matchEventDto.secondaryPlayer( toDto( event.getSecondaryPlayer() ) );
        matchEventDto.timestamp( event.getTimestamp() );
        matchEventDto.matchMinute( event.getMatchMinute() );
        Map<String, String> map = event.getData();
        if ( map != null ) {
            matchEventDto.data( new LinkedHashMap<String, String>( map ) );
        }

        matchEventDto.type( event.getType().name() );

        return matchEventDto.build();
    }

    @Override
    public TeamDto toDto(Team team) {
        if ( team == null ) {
            return null;
        }

        TeamDto.TeamDtoBuilder teamDto = TeamDto.builder();

        teamDto.id( team.getId() );
        teamDto.name( team.getName() );
        teamDto.shortName( team.getShortName() );
        teamDto.country( team.getCountry() );
        teamDto.logoUrl( team.getLogoUrl() );
        teamDto.players( playerListToPlayerDtoList( team.getPlayers() ) );

        return teamDto.build();
    }

    @Override
    public PlayerDto toDto(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerDto.PlayerDtoBuilder playerDto = PlayerDto.builder();

        playerDto.id( player.getId() );
        playerDto.name( player.getName() );
        playerDto.dateOfBirth( player.getDateOfBirth() );
        playerDto.nationality( player.getNationality() );
        playerDto.jerseyNumber( player.getJerseyNumber() );
        playerDto.photoUrl( player.getPhotoUrl() );

        playerDto.position( player.getPosition().getDisplayName() );
        playerDto.age( player.getAge() );

        return playerDto.build();
    }

    protected List<MatchEventDto> matchEventListToMatchEventDtoList(List<MatchEvent> list) {
        if ( list == null ) {
            return null;
        }

        List<MatchEventDto> list1 = new ArrayList<MatchEventDto>( list.size() );
        for ( MatchEvent matchEvent : list ) {
            list1.add( toEventDto( matchEvent ) );
        }

        return list1;
    }

    protected List<PlayerDto> playerListToPlayerDtoList(List<Player> list) {
        if ( list == null ) {
            return null;
        }

        List<PlayerDto> list1 = new ArrayList<PlayerDto>( list.size() );
        for ( Player player : list ) {
            list1.add( toDto( player ) );
        }

        return list1;
    }
}
