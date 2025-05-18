package com.localhost.pitchperfect.application.port.in;

import com.localhost.pitchperfect.application.dto.ChatMessageDto;

/**
 * Port for chat operations in the application.
 * This interface defines operations for managing chat messages.
 */
public interface ChatUseCase {
    
    /**
     * Processes and saves a chat message.
     *
     * @param roomId the ID of the chat room
     * @param message the chat message to process
     * @param userId the ID of the user sending the message
     * @return the processed chat message
     */
    ChatMessageDto processAndSaveMessage(String roomId, ChatMessageDto message, String userId);
    
    /**
     * Retrieves a username by user ID.
     *
     * @param userId the ID of the user
     * @return the username
     */
    String getUsernameById(String userId);
}
