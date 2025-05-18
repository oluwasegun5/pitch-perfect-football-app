# Component Details - Hexagonal Architecture

## Overview

The Pitch Perfect Football App follows the hexagonal architecture pattern (also known as ports and adapters). This architecture separates the application into loosely coupled components with explicit dependencies, making it more maintainable, testable, and adaptable to changing requirements.

## Architectural Layers

### 1. Domain Layer (Core)

The domain layer contains the business logic and domain models, completely isolated from external concerns.

#### Key Components:

- **Domain Models**
  - `Match`: Represents a football match with teams, score, status, etc.
  - `Team`: Contains team information, players, statistics
  - `Player`: Player details, statistics, and performance metrics
  - `User`: Application user information
  - `ChatRoom`: Represents a chat room for discussions
  - `Message`: Chat messages exchanged between users

- **Domain Services**
  - `MatchDomainService`: Core business logic for match management
  - `ChatDomainService`: Core business logic for chat functionality
  - `UserDomainService`: Core business logic for user management

### 2. Application Layer

The application layer orchestrates the flow of data and coordinates high-level operations.

#### Key Components:

- **Use Cases / Application Services**
  - `MatchService`: Coordinates match-related operations
  - `LiveScoreService`: Manages real-time score updates
  - `ChatService`: Handles chat room operations and messaging
  - `UserService`: Manages user-related operations
  - `NotificationService`: Coordinates sending notifications

- **Ports (Interfaces)**
  - **Inbound Ports** (Primary/Driving Adapters)
    - `MatchServicePort`: Interface for match operations
    - `ChatServicePort`: Interface for chat operations
    - `UserServicePort`: Interface for user operations
    - `NotificationServicePort`: Interface for notification operations
  
  - **Outbound Ports** (Secondary/Driven Adapters)
    - `MatchRepositoryPort`: Interface for match data persistence
    - `ChatRepositoryPort`: Interface for chat data persistence
    - `UserRepositoryPort`: Interface for user data persistence
    - `NotificationPort`: Interface for sending notifications
    - `ExternalDataProviderPort`: Interface for external football data

### 3. Infrastructure Layer

The infrastructure layer contains implementations of the interfaces defined in the application layer.

#### Key Components:

- **Inbound Adapters**
  - `RestController`: REST API endpoints
  - `WebSocketController`: WebSocket endpoints
  - `GraphQLResolver`: GraphQL resolvers (if applicable)

- **Outbound Adapters**
  - `PostgresMatchRepository`: PostgreSQL implementation of match repository
  - `PostgresChatRepository`: PostgreSQL implementation of chat repository
  - `PostgresUserRepository`: PostgreSQL implementation of user repository
  - `RedisNotificationAdapter`: Redis implementation of notification service
  - `ExternalAPIAdapter`: Implementation for external football data APIs

## Component Interactions

### Match Management Flow

1. External request comes through an inbound adapter (e.g., `MatchRestController`)
2. Request is forwarded to the appropriate application service (`MatchService`)
3. Application service uses domain services and models to process the request
4. Application service interacts with outbound ports (e.g., `MatchRepositoryPort`)
5. Outbound adapters (e.g., `PostgresMatchRepository`) handle external interactions
6. Response flows back through the layers to the client

### Real-time Updates Flow

1. External data provider sends updates through an adapter
2. Updates are processed by application services
3. Domain models are updated
4. Notification service is triggered
5. WebSocket adapter broadcasts updates to connected clients

## Cross-cutting Concerns

- **Security**: Implemented across layers with specific responsibilities
  - Authentication adapters in the infrastructure layer
  - Authorization logic in the application layer
  - Security utilities as shared components

- **Caching**: Implemented in infrastructure layer
  - Redis cache adapters
  - Cache invalidation strategies

- **Logging and Monitoring**: Spans across all layers
  - Aspect-oriented logging
  - Performance monitoring

- **Error Handling**: Consistent approach across layers
  - Domain exceptions in the domain layer
  - Application exceptions in the application layer
  - Infrastructure exceptions in the infrastructure layer
  - Global exception handling in the inbound adapters

## Dependency Management

The hexagonal architecture enforces a strict dependency rule: dependencies always point inward.

- Domain layer has no dependencies on other layers
- Application layer depends only on the domain layer
- Infrastructure layer depends on the application and domain layers

This ensures that the core business logic remains isolated and can be tested independently of external systems.
