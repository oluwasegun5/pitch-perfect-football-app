package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.domain.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA Repository for Match entities.
 */
@Repository
public interface MatchJpaRepository extends JpaRepository<MatchEntity, UUID> {
    
    List<MatchEntity> findByStatus(String status);
    
    List<MatchEntity> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    List<MatchEntity> findByHomeTeamIdOrAwayTeamId(UUID homeTeamId, UUID awayTeamId);
}
