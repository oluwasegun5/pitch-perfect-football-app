package com.localhost.pitchperfect.domain.event;

/**
 * Interface for publishing domain events.
 * This is a core domain component that allows publishing events from the domain layer.
 */
public interface DomainEventPublisher {
    
    /**
     * Publishes a domain event.
     *
     * @param eventType the type of the event
     * @param payload the event payload
     */
    void publish(String eventType, Object payload);
}
