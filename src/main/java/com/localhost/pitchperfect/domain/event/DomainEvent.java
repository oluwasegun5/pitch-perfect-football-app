package com.localhost.pitchperfect.domain.event;

/**
 * Interface for domain events.
 * All domain events should implement this interface.
 */
public interface DomainEvent {
    
    /**
     * Gets the timestamp when the event occurred.
     *
     * @return the timestamp in milliseconds
     */
    long getTimestamp();
    
    /**
     * Gets the type of the event.
     *
     * @return the event type
     */
    String getType();
}
