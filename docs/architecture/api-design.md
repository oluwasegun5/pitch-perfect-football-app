# API Design - Hexagonal Architecture

## Overview

This document outlines the API design for the Pitch Perfect Football App, following hexagonal architecture principles. The API serves as an inbound adapter, allowing external systems to interact with the application's core functionality.

## REST API Endpoints

### Authentication API

#### User Registration
- **Endpoint**: `POST /api/auth/register`
- **Description**: Register a new user
- **Request Body**:
  ```json
  {
    "username": "string",
    "email": "string",
    "password": "string",
    "firstName": "string",
    "lastName": "string"
  }
  ```
- **Response**: 
  ```json
  {
    "id": "uuid",
    "username": "string",
    "email": "string",
    "roles": ["string"],
    "token": "string"
  }
  ```
- **Status Codes**: 201 Created, 400 Bad Request

#### User Login
- **Endpoint**: `POST /api/auth/login`
- **Description**: Authenticate a user
- **Request Body**:
  ```json
  {
    "username": "string",
    "password": "string"
  }
  ```
- **Response**:
  ```json
  {
    "id": "uuid",
    "username": "string",
    "token": "string",
    "expiresAt": "timestamp"
  }
  ```
- **Status Codes**: 200 OK, 401 Unauthorized

#### Token Refresh
- **Endpoint**: `POST /api/auth/refresh`
- **Description**: Refresh authentication token
- **Request Headers**: `Authorization: Bearer {token}`
- **Response**:
  ```json
  {
    "token": "string",
    "expiresAt": "timestamp"
  }
  ```
- **Status Codes**: 200 OK, 401 Unauthorized

### Match API

#### Get All Matches
- **Endpoint**: `GET /api/matches`
- **Description**: Retrieve all matches with pagination
- **Query Parameters**: 
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)
  - `sort`: Sort field (default: startTime)
  - `status`: Filter by match status (SCHEDULED, LIVE, COMPLETED)
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "uuid",
        "homeTeam": {
          "id": "uuid",
          "name": "string",
          "logo": "url"
        },
        "awayTeam": {
          "id": "uuid",
          "name": "string",
          "logo": "url"
        },
        "score": {
          "homeScore": "integer",
          "awayScore": "integer"
        },
        "status": "string",
        "startTime": "timestamp",
        "venue": "string"
      }
    ],
    "totalElements": "integer",
    "totalPages": "integer",
    "currentPage": "integer"
  }
  ```
- **Status Codes**: 200 OK

#### Get Match Details
- **Endpoint**: `GET /api/matches/{matchId}`
- **Description**: Retrieve detailed information about a specific match
- **Path Parameters**: `matchId`: UUID of the match
- **Response**:
  ```json
  {
    "id": "uuid",
    "homeTeam": {
      "id": "uuid",
      "name": "string",
      "logo": "url",
      "players": [
        {
          "id": "uuid",
          "name": "string",
          "position": "string",
          "number": "integer"
        }
      ]
    },
    "awayTeam": {
      "id": "uuid",
      "name": "string",
      "logo": "url",
      "players": [
        {
          "id": "uuid",
          "name": "string",
          "position": "string",
          "number": "integer"
        }
      ]
    },
    "score": {
      "homeScore": "integer",
      "awayScore": "integer",
      "halfTimeScore": {
        "homeScore": "integer",
        "awayScore": "integer"
      }
    },
    "events": [
      {
        "id": "uuid",
        "type": "GOAL|YELLOW_CARD|RED_CARD|SUBSTITUTION",
        "minute": "integer",
        "player": {
          "id": "uuid",
          "name": "string"
        },
        "team": "HOME|AWAY",
        "additionalInfo": "string"
      }
    ],
    "statistics": {
      "possession": {
        "home": "integer",
        "away": "integer"
      },
      "shots": {
        "home": "integer",
        "away": "integer"
      },
      "shotsOnTarget": {
        "home": "integer",
        "away": "integer"
      },
      "corners": {
        "home": "integer",
        "away": "integer"
      },
      "fouls": {
        "home": "integer",
        "away": "integer"
      }
    },
    "status": "string",
    "startTime": "timestamp",
    "venue": "string",
    "referee": "string",
    "commentary": [
      {
        "id": "uuid",
        "minute": "integer",
        "text": "string",
        "type": "REGULAR|HIGHLIGHT"
      }
    ]
  }
  ```
- **Status Codes**: 200 OK, 404 Not Found

#### Create Match (Admin)
- **Endpoint**: `POST /api/admin/matches`
- **Description**: Create a new match
- **Request Headers**: `Authorization: Bearer {token}`
- **Request Body**:
  ```json
  {
    "homeTeamId": "uuid",
    "awayTeamId": "uuid",
    "startTime": "timestamp",
    "venue": "string",
    "referee": "string",
    "competition": "string"
  }
  ```
- **Response**: Match object
- **Status Codes**: 201 Created, 400 Bad Request, 401 Unauthorized, 403 Forbidden

#### Update Match (Admin)
- **Endpoint**: `PUT /api/admin/matches/{matchId}`
- **Description**: Update match details
- **Request Headers**: `Authorization: Bearer {token}`
- **Path Parameters**: `matchId`: UUID of the match
- **Request Body**: Match object
- **Response**: Updated match object
- **Status Codes**: 200 OK, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found

### Chat API

#### Get Chat Rooms
- **Endpoint**: `GET /api/chat/rooms`
- **Description**: Get all available chat rooms
- **Request Headers**: `Authorization: Bearer {token}`
- **Query Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 20)
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "uuid",
        "name": "string",
        "description": "string",
        "type": "MATCH|GENERAL|TEAM",
        "participantCount": "integer",
        "lastMessageAt": "timestamp"
      }
    ],
    "totalElements": "integer",
    "totalPages": "integer",
    "currentPage": "integer"
  }
  ```
- **Status Codes**: 200 OK, 401 Unauthorized

#### Get Chat Room Messages
- **Endpoint**: `GET /api/chat/rooms/{roomId}/messages`
- **Description**: Get messages for a specific chat room
- **Request Headers**: `Authorization: Bearer {token}`
- **Path Parameters**: `roomId`: UUID of the chat room
- **Query Parameters**:
  - `page`: Page number (default: 0)
  - `size`: Page size (default: 50)
  - `before`: Timestamp to get messages before
- **Response**:
  ```json
  {
    "content": [
      {
        "id": "uuid",
        "content": "string",
        "sender": {
          "id": "uuid",
          "username": "string",
          "avatar": "url"
        },
        "timestamp": "timestamp",
        "type": "TEXT|IMAGE|SYSTEM"
      }
    ],
    "totalElements": "integer",
    "totalPages": "integer",
    "currentPage": "integer"
  }
  ```
- **Status Codes**: 200 OK, 401 Unauthorized, 403 Forbidden, 404 Not Found

#### Send Message
- **Endpoint**: `POST /api/chat/rooms/{roomId}/messages`
- **Description**: Send a message to a chat room
- **Request Headers**: `Authorization: Bearer {token}`
- **Path Parameters**: `roomId`: UUID of the chat room
- **Request Body**:
  ```json
  {
    "content": "string",
    "type": "TEXT|IMAGE"
  }
  ```
- **Response**: Created message object
- **Status Codes**: 201 Created, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found

### User API

#### Get User Profile
- **Endpoint**: `GET /api/users/me`
- **Description**: Get current user's profile
- **Request Headers**: `Authorization: Bearer {token}`
- **Response**:
  ```json
  {
    "id": "uuid",
    "username": "string",
    "email": "string",
    "firstName": "string",
    "lastName": "string",
    "avatar": "url",
    "preferences": {
      "favoriteTeams": ["uuid"],
      "notificationSettings": {
        "matchStart": "boolean",
        "goals": "boolean",
        "finalResults": "boolean"
      },
      "theme": "LIGHT|DARK|SYSTEM"
    },
    "createdAt": "timestamp"
  }
  ```
- **Status Codes**: 200 OK, 401 Unauthorized

#### Update User Profile
- **Endpoint**: `PUT /api/users/me`
- **Description**: Update current user's profile
- **Request Headers**: `Authorization: Bearer {token}`
- **Request Body**: User profile object
- **Response**: Updated user profile
- **Status Codes**: 200 OK, 400 Bad Request, 401 Unauthorized

## WebSocket API

WebSocket connections are established at `/ws` with STOMP protocol.

### Connection
- **Endpoint**: `/ws`
- **Authentication**: JWT token as a query parameter or in the headers

### Subscriptions

#### Match Updates
- **Topic**: `/topic/matches/{matchId}`
- **Description**: Real-time updates for a specific match
- **Message Format**:
  ```json
  {
    "type": "SCORE_UPDATE|MATCH_EVENT|STATISTICS_UPDATE|COMMENTARY",
    "timestamp": "timestamp",
    "data": {
      // Depends on the update type
    }
  }
  ```

#### Chat Messages
- **Topic**: `/topic/chat/{roomId}`
- **Description**: Real-time messages for a specific chat room
- **Message Format**:
  ```json
  {
    "id": "uuid",
    "content": "string",
    "sender": {
      "id": "uuid",
      "username": "string",
      "avatar": "url"
    },
    "timestamp": "timestamp",
    "type": "TEXT|IMAGE|SYSTEM"
  }
  ```

#### User Notifications
- **Topic**: `/user/queue/notifications`
- **Description**: User-specific notifications
- **Message Format**:
  ```json
  {
    "id": "uuid",
    "type": "MATCH_START|GOAL|MATCH_END|CHAT_MESSAGE",
    "title": "string",
    "message": "string",
    "data": {
      // Additional data specific to the notification type
    },
    "timestamp": "timestamp"
  }
  ```

### Publishing

#### Send Chat Message
- **Destination**: `/app/chat/{roomId}`
- **Message Format**:
  ```json
  {
    "content": "string",
    "type": "TEXT|IMAGE"
  }
  ```

#### User Typing Indicator
- **Destination**: `/app/chat/{roomId}/typing`
- **Message Format**:
  ```json
  {
    "typing": "boolean"
  }
  ```

## API Versioning

The API uses URI versioning:
- Current version: `/api/v1/...`
- Future versions: `/api/v2/...`

## Error Handling

All endpoints return standardized error responses:

```json
{
  "timestamp": "timestamp",
  "status": "integer",
  "error": "string",
  "message": "string",
  "path": "string",
  "details": [
    {
      "field": "string",
      "message": "string"
    }
  ]
}
```

## Rate Limiting

- API requests are limited to 100 requests per minute per user
- WebSocket connections are limited to 5 concurrent connections per user

## Documentation

API documentation is available at `/swagger-ui.html` using OpenAPI 3.0 specification.
