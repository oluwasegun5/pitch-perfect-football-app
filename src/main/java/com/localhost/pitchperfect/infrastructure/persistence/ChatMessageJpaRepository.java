package com.localhost.pitchperfect.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA repository for chat message entities.
 * Provides database operations for chat messages.
 */
@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, String> {
    
    /**
     * Find all messages for a specific chat room, ordered by timestamp.
     *
     * @param roomId the ID of the chat room
     * @return list of chat message entities
     */
    List<ChatMessageEntity> findByRoomIdOrderByTimestampAsc(String roomId);
    
    /**
     * Find the most recent messages for a specific chat room, limited by count.
     *
     * @param roomId the ID of the chat room
     * @param limit the maximum number of messages to return
     * @return list of chat message entities
     */
    List<ChatMessageEntity> findTop50ByRoomIdOrderByTimestampDesc(String roomId);
    
    /**
     * Delete all messages for a specific chat room.
     *
     * @param roomId the ID of the chat room
     */
    void deleteByRoomId(String roomId);
}
