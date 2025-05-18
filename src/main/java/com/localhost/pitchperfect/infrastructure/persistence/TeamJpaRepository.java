package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.domain.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA Repository for Player entities.
 */
@Repository
public interface TeamJpaRepository extends JpaRepository<TeamEntity, UUID> {
    
    List<TeamEntity> findByCountry(String country);
    
    Optional<TeamEntity> findByName(String name);
}
