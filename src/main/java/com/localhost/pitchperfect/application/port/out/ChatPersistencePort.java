package com.localhost.pitchperfect.application.port.out;

import com.localhost.pitchperfect.application.dto.ChatMessageDto;

/**
 * Port for chat persistence operations.
 * This interface defines operations for storing and retrieving chat messages.
 */
public interface ChatPersistencePort {
    
    /**
     * Saves a chat message.
     *
     * @param roomId the ID of the chat room
     * @param message the chat message to save
     * @return the saved chat message
     */
    ChatMessageDto saveMessage(String roomId, ChatMessageDto message);
    
    /**
     * Retrieves a username by user ID.
     *
     * @param userId the ID of the user
     * @return the username
     */
    String getUsernameById(String userId);
    
    /**
     * Retrieves a user's avatar URL by user ID.
     *
     * @param userId the ID of the user
     * @return the avatar URL
     */
    String getUserAvatarById(String userId);
}
