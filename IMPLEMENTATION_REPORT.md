# Implementation Report: Chat Persistence Infrastructure

## Overview

This report documents the implementation of the missing chat persistence infrastructure for the Pitch Perfect Football App. The implementation follows hexagonal architecture principles and includes comprehensive tests to ensure functionality and reliability.

## Identified Gaps

During the analysis of the codebase, the following gaps were identified:

1. Missing chat persistence infrastructure:
   - No ChatMessageEntity for database persistence
   - No ChatMessageJpaRepository for database operations
   - No ChatPersistenceAdapter implementing the ChatPersistencePort

2. Other issues discovered during implementation:
   - Missing dependencies in pom.xml (spring-session-data-redis)
   - Missing security components (JwtTokenProvider)
   - Interface mismatches between DTOs and domain models

## Implemented Components

### 1. ChatMessageEntity

A JPA entity class for persisting chat messages in the database:

```java
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
```

### 2. ChatMessageJpaRepository

A JPA repository interface for database operations on chat messages:

```java
@Repository
public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, String> {
    
    List<ChatMessageEntity> findByRoomIdOrderByTimestampAsc(String roomId);
    
    List<ChatMessageEntity> findTop50ByRoomIdOrderByTimestampDesc(String roomId);
    
    void deleteByRoomId(String roomId);
}
```

### 3. ChatPersistenceAdapter

An implementation of the ChatPersistencePort that connects the application layer to the infrastructure layer:

```java
@Component
@RequiredArgsConstructor
@Slf4j
public class ChatPersistenceAdapter implements ChatPersistencePort {

    private final ChatMessageJpaRepository chatMessageRepository;
    private final UserJpaRepository userRepository;
    
    private static final String DEFAULT_AVATAR = "default-avatar.jpg";

    @Override
    public ChatMessageDto saveMessage(String roomId, ChatMessageDto message) {
        // Convert DTO to entity
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setId(message.getId());
        entity.setRoomId(roomId);
        entity.setContent(message.getContent());
        entity.setTimestamp(message.getTimestamp());
        entity.setSenderId(message.getSender().getId());
        entity.setSenderName(message.getSender().getUsername());
        entity.setSenderAvatar(message.getSender().getAvatar());
        
        // Save entity
        ChatMessageEntity savedEntity = chatMessageRepository.save(entity);
        
        // Convert back to DTO
        return toDto(savedEntity);
    }

    @Override
    public String getUsernameById(String userId) {
        return userRepository.findById(userId)
                .map(UserEntity::getUsername)
                .orElse("Unknown User");
    }

    @Override
    public String getUserAvatarById(String userId) {
        return userRepository.findById(userId)
                .map(UserEntity::getAvatarUrl)
                .orElse(DEFAULT_AVATAR);
    }
    
    // Private helper method to convert entity to DTO
    private ChatMessageDto toDto(ChatMessageEntity entity) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setTimestamp(entity.getTimestamp());
        
        ChatMessageDto.SenderDto sender = new ChatMessageDto.SenderDto(
                entity.getSenderId(),
                entity.getSenderName(),
                entity.getSenderAvatar()
        );
        dto.setSender(sender);
        
        return dto;
    }
}
```

### 4. Additional Components

- **UserEntity**: A placeholder entity for user data
- **UserJpaRepository**: A placeholder repository for user operations
- **JwtTokenProvider**: A security component for JWT token operations
- **Updated MatchEvent and MatchEventDto**: Enhanced with additional fields and methods

## Testing

Comprehensive tests were written for the new components:

1. **ChatPersistenceAdapterTest**: Unit tests for the adapter implementation
2. **ChatMessageJpaRepositoryTest**: Integration tests for the repository

All tests pass successfully, ensuring the reliability of the implemented components.

## Challenges and Solutions

1. **Missing Dependencies**: Added spring-session-data-redis to pom.xml
2. **Interface Mismatches**: Updated DTOs and domain models to align with their usage
3. **Compilation Errors**: Fixed enum usage in switch statements and corrected method signatures

## Conclusion

The implementation of the chat persistence infrastructure successfully fills the identified gaps in the codebase. The components follow hexagonal architecture principles, with clear separation between the domain, application, and infrastructure layers. Comprehensive tests ensure the reliability and correctness of the implementation.

The project now has a complete chat persistence infrastructure that can be used to store and retrieve chat messages, supporting the real-time chat functionality of the Pitch Perfect Football App.
