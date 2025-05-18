package com.localhost.pitchperfect.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for user entities.
 * This is a placeholder implementation since the actual UserJpaRepository was not found in the codebase.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, String> {
    
    /**
     * Find a user by username.
     *
     * @param username the username to search for
     * @return optional containing the user if found
     */
    Optional<UserEntity> findByUsername(String username);
    
    /**
     * Check if a user with the given username exists.
     *
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Find a user by email.
     *
     * @param email the email to search for
     * @return optional containing the user if found
     */
    Optional<UserEntity> findByEmail(String email);
}
