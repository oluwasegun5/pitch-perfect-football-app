package com.localhost.pitchperfect.application.port.in;

/**
 * Port for tracking user presence in the application.
 * This interface defines operations for managing user presence status.
 */
public interface PresenceUseCase {
    
    /**
     * Registers a user as connected to the application.
     *
     * @param userId the ID of the user who connected
     */
    void userConnected(String userId);
    
    /**
     * Registers a user as disconnected from the application.
     *
     * @param userId the ID of the user who disconnected
     */
    void userDisconnected(String userId);
    
    /**
     * Registers a user as having joined a specific room.
     *
     * @param userId the ID of the user who joined
     * @param roomId the ID of the room that was joined
     */
    void userJoinedRoom(String userId, String roomId);
    
    /**
     * Registers a user as having left a specific room.
     *
     * @param userId the ID of the user who left
     * @param roomId the ID of the room that was left
     */
    void userLeftRoom(String userId, String roomId);
    
    /**
     * Retrieves the room ID associated with a user's subscription.
     *
     * @param userId the ID of the user
     * @param subscriptionId the ID of the subscription
     * @return the room ID, or null if not found
     */
    String getRoomIdBySubscription(String userId, String subscriptionId);
}
