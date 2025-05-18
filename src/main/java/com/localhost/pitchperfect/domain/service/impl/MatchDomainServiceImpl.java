package com.localhost.pitchperfect.domain.service.impl;

import com.localhost.pitchperfect.domain.model.Match;
import com.localhost.pitchperfect.domain.model.MatchEvent;
import com.localhost.pitchperfect.domain.model.Player;
import com.localhost.pitchperfect.domain.service.MatchDomainService;

/**
 * Implementation of the MatchDomainService interface.
 * Contains core business logic related to matches.
 */
public class MatchDomainServiceImpl implements MatchDomainService {
    
    @Override
    public Match startMatch(Match match) {
        match.start();
        return match;
    }
    
    @Override
    public Match completeMatch(Match match) {
        match.complete();
        return match;
    }
    
    @Override
    public Match cancelMatch(Match match, String reason) {
        match.cancel(reason);
        return match;
    }
    
    @Override
    public Match addGoal(Match match, Player scorer, Player assistant, boolean isHomeTeam) {
        if (isHomeTeam) {
            match.addHomeGoal(scorer, assistant);
        } else {
            match.addAwayGoal(scorer, assistant);
        }
        return match;
    }
    
    @Override
    public Match addEvent(Match match, MatchEvent event) {
        match.addEvent(event);
        return match;
    }
    
    @Override
    public Match updateScore(Match match, int homeScore, int awayScore) {
        match.updateScore(homeScore, awayScore);
        return match;
    }
}
