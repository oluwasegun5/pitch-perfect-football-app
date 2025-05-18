package com.localhost.pitchperfect.infrastructure.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

/**
 * Entity class for user data.
 * This is a placeholder implementation since the actual UserEntity was not found in the codebase.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false, unique = true)
    private String username;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Column(nullable = false)
    private String email;
    
    // Other user properties would go here
}
