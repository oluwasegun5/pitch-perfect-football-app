package com.localhost.pitchperfect.infrastructure.persistence;

import com.localhost.pitchperfect.domain.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA Repository for Player entities.
 */
@Repository
public interface PlayerJpaRepository extends JpaRepository<PlayerEntity, UUID> {
    
    List<PlayerEntity> findByTeamId(UUID teamId);
    
    List<PlayerEntity> findByPosition(String position);
    
    List<PlayerEntity> findByNationality(String nationality);
}
