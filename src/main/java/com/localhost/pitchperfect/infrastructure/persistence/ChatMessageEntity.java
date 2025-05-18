package com.localhost.pitchperfect.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Entity class for chat messages.
 * Represents the persistence model for chat messages in the database.
 */
@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageEntity {

    @Id
    private String id;
    
    @Column(name = "room_id", nullable = false)
    private String roomId;
    
    @Column(nullable = false, length = 2000)
    private String content;
    
    @Column(nullable = false)
    private Instant timestamp;
    
    @Column(name = "sender_id", nullable = false)
    private String senderId;
    
    @Column(name = "sender_name", nullable = false)
    private String senderName;
    
    @Column(name = "sender_avatar")
    private String senderAvatar;
}
