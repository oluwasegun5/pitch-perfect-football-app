# WebSocket Architecture - Hexagonal Architecture

## Overview

This document outlines the WebSocket architecture for the Pitch Perfect Football App, designed according to hexagonal architecture principles. The WebSocket functionality serves as both an inbound and outbound adapter, enabling real-time bidirectional communication between clients and the application core.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Applications                       │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      WebSocket Connection                        │
│                                                                 │
│  ┌─────────────────┐      ┌─────────────────┐      ┌──────────┐ │
│  │ Authentication  │      │ STOMP Protocol  │      │ SockJS   │ │
│  │    Handler      │◄────►│     Layer       │◄────►│  Layer   │ │
│  └─────────────────┘      └─────────────────┘      └──────────┘ │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                     Infrastructure Layer                         │
│                                                                 │
│  ┌─────────────────┐      ┌─────────────────┐      ┌──────────┐ │
│  │   WebSocket     │      │  Message Broker │      │  Redis   │ │
│  │   Controller    │◄────►│  (In-memory/    │◄────►│ Session  │ │
│  │   (Adapter)     │      │   Redis)        │      │  Store   │ │
│  └────────┬────────┘      └─────────────────┘      └──────────┘ │
└───────────┼──────────────────────────────────────────────────────┘
            │
            ▼
┌───────────┴──────────────────────────────────────────────────────┐
│                      Application Layer                            │
│                                                                  │
│  ┌─────────────────┐      ┌─────────────────┐      ┌───────────┐ │
│  │   WebSocket     │      │   Application   │      │  Message  │ │
│  │   Service Port  │◄────►│    Services     │◄────►│ Validation│ │
│  │   (Interface)   │      │                 │      │           │ │
│  └─────────────────┘      └─────────────────┘      └───────────┘ │
└───────────────────────────────┬──────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────────┐
│                        Domain Layer                             │
│                                                                 │
│  ┌─────────────────┐      ┌─────────────────┐      ┌──────────┐ │
│  │  Domain Events  │      │  Domain Models  │      │ Domain   │ │
│  │                 │◄────►│                 │◄────►│ Services │ │
│  │                 │      │                 │      │          │ │
│  └─────────────────┘      └─────────────────┘      └──────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## WebSocket Components

### 1. Client-Side Components

- **WebSocket Client**: Browser or mobile app that establishes WebSocket connections
- **STOMP Client**: Implements the STOMP protocol for message formatting
- **SockJS Client**: Provides fallback options when WebSockets aren't supported

### 2. Infrastructure Layer Components (Adapters)

- **WebSocket Controller**: Handles incoming WebSocket connections and messages
- **Message Broker**: Routes messages between clients and application services
- **Session Management**: Tracks active WebSocket sessions and their subscriptions
- **Authentication Handler**: Validates user credentials and maintains session security

### 3. Application Layer Components (Ports)

- **WebSocket Service Port**: Interface defining WebSocket operations
- **Message Validation Service**: Validates incoming message format and content
- **Application Services**: Process WebSocket messages and trigger domain operations

### 4. Domain Layer Components

- **Domain Events**: Represent business events that can trigger WebSocket messages
- **Domain Models**: Core business entities
- **Domain Services**: Core business logic

## WebSocket Communication Flows

### 1. Connection Establishment

```
Client                                                 Server
  │                                                      │
  │  WebSocket Handshake (HTTP Upgrade)                  │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
  │  101 Switching Protocols                             │
  │ <─────────────────────────────────────────────────────
  │                                                      │
  │  STOMP CONNECT Frame                                 │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
  │  STOMP CONNECTED Frame                               │
  │ <─────────────────────────────────────────────────────
  │                                                      │
```

1. Client initiates WebSocket connection with authentication token
2. Server validates token and establishes WebSocket connection
3. Client sends STOMP CONNECT frame
4. Server acknowledges with STOMP CONNECTED frame

### 2. Subscription Flow

```
Client                                                 Server
  │                                                      │
  │  STOMP SUBSCRIBE (topic/matches/{matchId})           │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
  │  Store subscription in session                       │
  │                                                      │
  │  STOMP RECEIPT                                       │
  │ <─────────────────────────────────────────────────────
  │                                                      │
```

1. Client subscribes to a topic (e.g., match updates, chat messages)
2. Server registers subscription and associates it with the client session
3. Server acknowledges subscription with receipt

### 3. Server-to-Client Message Flow

```
Domain Event                                          Client
  │                                                      │
  │  Event published to Application Layer                │
  │                                                      │
  │  Application Service processes event                 │
  │                                                      │
  │  WebSocket Service sends message to subscribers      │
  │                                                      │
  │  STOMP MESSAGE Frame                                 │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
```

1. Domain event occurs (e.g., goal scored, new chat message)
2. Event is published to the Application Layer
3. Application Service processes the event
4. WebSocket Service sends message to all subscribed clients

### 4. Client-to-Server Message Flow

```
Client                                                 Server
  │                                                      │
  │  STOMP SEND Frame                                    │
  │ ─────────────────────────────────────────────────────>
  │                                                      │
  │  Message validation                                  │
  │                                                      │
  │  Process message in Application Layer                │
  │                                                      │
  │  Update Domain Model                                 │
  │                                                      │
  │  Broadcast to other subscribers                      │
  │ <─────────────────────────────────────────────────────
  │                                                      │
```

1. Client sends message (e.g., chat message, typing indicator)
2. Server validates message format and content
3. Application Service processes the message
4. Domain Model is updated if necessary
5. Message is broadcast to other subscribers if applicable

## WebSocket Topics and Message Formats

### Match Updates

- **Topic**: `/topic/matches/{matchId}`
- **Purpose**: Real-time updates for a specific match
- **Message Format**:
  ```json
  {
    "type": "SCORE_UPDATE|MATCH_EVENT|STATISTICS_UPDATE|COMMENTARY",
    "timestamp": "ISO-8601 timestamp",
    "data": {
      // Content depends on update type
    }
  }
  ```

### Chat Messages

- **Topic**: `/topic/chat/{roomId}`
- **Purpose**: Real-time messages for a specific chat room
- **Message Format**:
  ```json
  {
    "id": "UUID",
    "content": "Message content",
    "sender": {
      "id": "UUID",
      "username": "Username",
      "avatar": "URL"
    },
    "timestamp": "ISO-8601 timestamp",
    "type": "TEXT|IMAGE|SYSTEM"
  }
  ```

### User Notifications

- **Topic**: `/user/queue/notifications`
- **Purpose**: User-specific notifications
- **Message Format**:
  ```json
  {
    "id": "UUID",
    "type": "MATCH_START|GOAL|MATCH_END|CHAT_MESSAGE",
    "title": "Notification title",
    "message": "Notification message",
    "data": {
      // Additional data specific to notification type
    },
    "timestamp": "ISO-8601 timestamp"
  }
  ```

### User Presence

- **Topic**: `/topic/presence/{roomId}`
- **Purpose**: Track users present in a chat room
- **Message Format**:
  ```json
  {
    "userId": "UUID",
    "username": "Username",
    "status": "JOINED|LEFT|ACTIVE|INACTIVE",
    "timestamp": "ISO-8601 timestamp"
  }
  ```

## Security Considerations

### Authentication

- JWT token validation for WebSocket connections
- Token passed as query parameter or in handshake headers
- Session invalidation on token expiration

### Authorization

- Topic-level access control
- Validation of subscription requests against user permissions
- Prevention of cross-user message interception

### Rate Limiting

- Connection rate limiting (max connections per user)
- Message rate limiting (max messages per minute)
- Subscription rate limiting (max subscriptions per connection)

### Message Validation

- Schema validation for all incoming messages
- Content sanitization to prevent XSS attacks
- Size limits for message payloads

## Scalability Considerations

### Session Affinity

- WebSocket connections maintain session affinity to servers
- Redis used for distributed session storage

### Horizontal Scaling

- Stateless design allows for horizontal scaling
- Message broker handles routing between server instances

### Connection Pooling

- Connection pooling for backend services
- Graceful handling of connection limits

## Monitoring and Metrics

- Connection count metrics
- Message throughput metrics
- Error rate monitoring
- Latency tracking

## Error Handling

- Automatic reconnection with exponential backoff
- Error frames for client notification
- Consistent error format:
  ```json
  {
    "error": "ERROR_CODE",
    "message": "Human-readable message",
    "timestamp": "ISO-8601 timestamp"
  }
  ```

## Implementation Details

### Spring WebSocket Configuration

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .withSockJS();
    }
    
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(
                    message, StompHeaderAccessor.class);
                
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    // Authentication logic
                }
                
                return message;
            }
        });
    }
}
```

### WebSocket Controller (Adapter)

```java
@Controller
public class WebSocketController {

    private final WebSocketService webSocketService;
    
    @Autowired
    public WebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }
    
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessage sendMessage(@DestinationVariable String roomId, 
                                  ChatMessage message, 
                                  Principal principal) {
        return webSocketService.processAndSaveMessage(roomId, message, principal.getName());
    }
    
    @MessageMapping("/chat/{roomId}/typing")
    @SendTo("/topic/chat/{roomId}/typing")
    public TypingIndicator sendTypingIndicator(@DestinationVariable String roomId,
                                             TypingIndicator indicator,
                                             Principal principal) {
        indicator.setUsername(principal.getName());
        return indicator;
    }
}
```

### WebSocket Service Port (Interface)

```java
public interface WebSocketService {
    ChatMessage processAndSaveMessage(String roomId, ChatMessage message, String username);
    void notifyMatchUpdate(String matchId, MatchUpdate update);
    void sendUserNotification(String userId, Notification notification);
    void trackUserPresence(String roomId, String userId, PresenceStatus status);
}
```

### WebSocket Service Implementation (Application Layer)

```java
@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate,
                              ChatMessageRepository chatMessageRepository,
                              UserRepository userRepository) {
        this.messagingTemplate = messagingTemplate;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }
    
    @Override
    public ChatMessage processAndSaveMessage(String roomId, ChatMessage message, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            
        message.setSender(new ChatMessage.Sender(user.getId(), user.getUsername(), user.getAvatar()));
        message.setTimestamp(Instant.now());
        
        // Save message to database
        chatMessageRepository.save(message);
        
        return message;
    }
    
    @Override
    public void notifyMatchUpdate(String matchId, MatchUpdate update) {
        messagingTemplate.convertAndSend("/topic/matches/" + matchId, update);
    }
    
    @Override
    public void sendUserNotification(String userId, Notification notification) {
        messagingTemplate.convertAndSendToUser(userId, "/queue/notifications", notification);
    }
    
    @Override
    public void trackUserPresence(String roomId, String userId, PresenceStatus status) {
        PresenceMessage presenceMessage = new PresenceMessage(userId, status);
        messagingTemplate.convertAndSend("/topic/presence/" + roomId, presenceMessage);
    }
}
```

## Testing Strategy

- Unit tests for WebSocket service implementations
- Integration tests for WebSocket controllers
- End-to-end tests with STOMP client
- Load testing for connection and message throughput

## Conclusion

The WebSocket architecture for the Pitch Perfect Football App follows hexagonal architecture principles, separating concerns between the domain core, application services, and infrastructure adapters. This design ensures that the real-time communication functionality is scalable, maintainable, and can evolve independently of the core business logic.
