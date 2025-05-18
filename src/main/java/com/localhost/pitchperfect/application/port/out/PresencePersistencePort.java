package com.localhost.pitchperfect.application.port.out;

import com.localhost.pitchperfect.application.dto.PresenceStatusDto;

/**
 * Port for persisting presence-related data.
 * This interface defines operations for storing and retrieving presence information.
 */
public interface PresencePersistencePort {
    
    /**
     * Saves a user's current status.
     *
     * @param userId the ID of the user
     * @param status the current presence status
     */
    void saveUserStatus(String userId, PresenceStatusDto status);
    
    /**
     * Retrieves a username by user ID.
     *
     * @param userId the ID of the user
     * @return the username
     */
    String getUsernameById(String userId);
    
    /**
     * Saves a user's subscription to a room.
     *
     * @param userId the ID of the user
     * @param subscriptionId the ID of the subscription
     * @param roomId the ID of the room
     */
    void saveUserSubscription(String userId, String subscriptionId, String roomId);
    
    /**
     * Removes a user's subscription.
     *
     * @param userId the ID of the user
     * @param subscriptionId the ID of the subscription
     */
    void removeUserSubscription(String userId, String subscriptionId);
    
    /**
     * Removes all subscriptions for a user.
     *
     * @param userId the ID of the user
     */
    void removeAllUserSubscriptions(String userId);
    
    /**
     * Retrieves the room ID associated with a subscription.
     *
     * @param userId the ID of the user
     * @param subscriptionId the ID of the subscription
     * @return the room ID, or null if not found
     */
    String getRoomIdBySubscription(String userId, String subscriptionId);
}
