# Database Schema - Hexagonal Architecture

## Overview

This document outlines the database schema for the Pitch Perfect Football App, designed to support the hexagonal architecture pattern. The database serves as an outbound adapter, providing persistence capabilities to the application's core domain.

## Database Technology

The application uses PostgreSQL as the primary relational database for persistent storage and Redis for caching and session management.

## Entity Relationship Diagram

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    Users    │       │    Teams    │       │   Players   │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id          │       │ id          │       │ id          │
│ username    │       │ name        │       │ name        │
│ email       │       │ logo        │       │ team_id     │
│ password    │       │ country     │       │ position    │
│ first_name  │       │ founded     │       │ jersey_num  │
│ last_name   │       │ stadium     │       │ nationality │
│ avatar      │       │ description │       │ birth_date  │
│ created_at  │       │ created_at  │       │ height      │
│ updated_at  │       │ updated_at  │       │ weight      │
└─────┬───────┘       └──────┬──────┘       │ created_at  │
      │                      │              │ updated_at  │
      │                      └──────────────┼──────────┐  │
      │                                     │          │  │
┌─────┴───────┐       ┌─────────────┐       │          │  │
│ User_Roles  │       │   Matches   │       │          │  │
├─────────────┤       ├─────────────┤       │          │  │
│ user_id     │       │ id          │◄──────┘          │  │
│ role        │       │ home_team_id│                  │  │
└─────────────┘       │ away_team_id│◄─────────────────┘  │
                      │ start_time  │                     │
┌─────────────┐       │ venue       │       ┌─────────────┤
│ User_Prefs  │       │ status      │       │ Player_Stats│
├─────────────┤       │ referee     │       ├─────────────┤
│ user_id     │       │ competition │       │ player_id   │
│ pref_key    │       │ created_at  │       │ match_id    │
│ pref_value  │       │ updated_at  │       │ goals       │
└─────────────┘       └──────┬──────┘       │ assists     │
                             │              │ yellow_cards│
                             │              │ red_cards   │
┌─────────────┐       ┌──────┴──────┐       │ minutes     │
│ Chat_Rooms  │       │ Match_Events│       │ rating      │
├─────────────┤       ├─────────────┤       └─────────────┘
│ id          │       │ id          │
│ name        │       │ match_id    │
│ description │       │ type        │
│ type        │       │ minute      │
│ created_at  │       │ player_id   │
│ updated_at  │       │ team_id     │
└──────┬──────┘       │ description │
       │              │ created_at  │
       │              └─────────────┘
┌──────┴──────┐
│  Messages   │       ┌─────────────┐
├─────────────┤       │Match_Stats  │
│ id          │       ├─────────────┤
│ chat_room_id│       │ match_id    │
│ user_id     │       │ team_id     │
│ content     │       │ possession  │
│ type        │       │ shots       │
│ created_at  │       │ shots_target│
└─────────────┘       │ corners     │
                      │ fouls       │
                      │ offsides    │
                      │ created_at  │
                      │ updated_at  │
                      └─────────────┘
```

## Table Definitions

### Users

Stores user account information.

| Column      | Type         | Constraints       | Description                    |
|-------------|--------------|-------------------|--------------------------------|
| id          | UUID         | PK                | Unique identifier              |
| username    | VARCHAR(50)  | UNIQUE, NOT NULL  | User's username                |
| email       | VARCHAR(100) | UNIQUE, NOT NULL  | User's email address           |
| password    | VARCHAR(255) | NOT NULL          | Hashed password                |
| first_name  | VARCHAR(50)  |                   | User's first name              |
| last_name   | VARCHAR(50)  |                   | User's last name               |
| avatar      | VARCHAR(255) |                   | URL to user's avatar image     |
| created_at  | TIMESTAMP    | NOT NULL          | Account creation timestamp     |
| updated_at  | TIMESTAMP    | NOT NULL          | Last update timestamp          |

### User_Roles

Maps users to their roles for authorization.

| Column      | Type         | Constraints       | Description                    |
|-------------|--------------|-------------------|--------------------------------|
| user_id     | UUID         | FK, NOT NULL      | Reference to Users table       |
| role        | VARCHAR(20)  | NOT NULL          | Role name (USER, ADMIN, etc.)  |

### User_Preferences

Stores user preferences in a key-value format.

| Column      | Type         | Constraints       | Description                    |
|-------------|--------------|-------------------|--------------------------------|
| user_id     | UUID         | FK, NOT NULL      | Reference to Users table       |
| pref_key    | VARCHAR(50)  | NOT NULL          | Preference key                 |
| pref_value  | TEXT         |                   | Preference value               |

### Teams

Stores football team information.

| Column      | Type         | Constraints       | Description                    |
|-------------|--------------|-------------------|--------------------------------|
| id          | UUID         | PK                | Unique identifier              |
| name        | VARCHAR(100) | NOT NULL          | Team name                      |
| logo        | VARCHAR(255) |                   | URL to team logo               |
| country     | VARCHAR(50)  |                   | Team's country                 |
| founded     | INTEGER      |                   | Year the team was founded      |
| stadium     | VARCHAR(100) |                   | Home stadium name              |
| description | TEXT         |                   | Team description               |
| created_at  | TIMESTAMP    | NOT NULL          | Record creation timestamp      |
| updated_at  | TIMESTAMP    | NOT NULL          | Last update timestamp          |

### Players

Stores player information.

| Column      | Type         | Constraints       | Description                    |
|-------------|--------------|-------------------|--------------------------------|
| id          | UUID         | PK                | Unique identifier              |
| name        | VARCHAR(100) | NOT NULL          | Player's full name             |
| team_id     | UUID         | FK                | Reference to Teams table       |
| position    | VARCHAR(20)  |                   | Player's position              |
| jersey_num  | INTEGER      |                   | Jersey number                  |
| nationality | VARCHAR(50)  |                   | Player's nationality           |
| birth_date  | DATE         |                   | Player's date of birth         |
| height      | INTEGER      |                   | Height in cm                   |
| weight      | INTEGER      |                   | Weight in kg                   |
| created_at  | TIMESTAMP    | NOT NULL          | Record creation timestamp      |
| updated_at  | TIMESTAMP    | NOT NULL          | Last update timestamp          |

### Matches

Stores football match information.

| Column       | Type         | Constraints       | Description                    |
|--------------|--------------|-------------------|--------------------------------|
| id           | UUID         | PK                | Unique identifier              |
| home_team_id | UUID         | FK, NOT NULL      | Reference to Teams table       |
| away_team_id | UUID         | FK, NOT NULL      | Reference to Teams table       |
| start_time   | TIMESTAMP    | NOT NULL          | Match start time               |
| venue        | VARCHAR(100) |                   | Match venue/stadium            |
| status       | VARCHAR(20)  | NOT NULL          | Match status                   |
| referee      | VARCHAR(100) |                   | Match referee                  |
| competition  | VARCHAR(100) |                   | Competition name               |
| created_at   | TIMESTAMP    | NOT NULL          | Record creation timestamp      |
| updated_at   | TIMESTAMP    | NOT NULL          | Last update timestamp          |

### Match_Events

Stores events that occur during matches.

| Column      | Type         | Constraints       | Description                    |
|-------------|--------------|-------------------|--------------------------------|
| id          | UUID         | PK                | Unique identifier              |
| match_id    | UUID         | FK, NOT NULL      | Reference to Matches table     |
| type        | VARCHAR(20)  | NOT NULL          | Event type                     |
| minute      | INTEGER      | NOT NULL          | Minute when event occurred     |
| player_id   | UUID         | FK                | Reference to Players table     |
| team_id     | UUID         | FK                | Reference to Teams table       |
| description | TEXT         |                   | Event description              |
| created_at  | TIMESTAMP    | NOT NULL          | Record creation timestamp      |

### Match_Statistics

Stores statistical data for matches.

| Column       | Type         | Constraints       | Description                    |
|--------------|--------------|-------------------|--------------------------------|
| match_id     | UUID         | FK, NOT NULL      | Reference to Matches table     |
| team_id      | UUID         | FK, NOT NULL      | Reference to Teams table       |
| possession   | INTEGER      |                   | Possession percentage          |
| shots        | INTEGER      |                   | Total shots                    |
| shots_target | INTEGER      |                   | Shots on target                |
| corners      | INTEGER      |                   | Corner kicks                   |
| fouls        | INTEGER      |                   | Fouls committed                |
| offsides     | INTEGER      |                   | Offsides                       |
| created_at   | TIMESTAMP    | NOT NULL          | Record creation timestamp      |
| updated_at   | TIMESTAMP    | NOT NULL          | Last update timestamp          |

### Player_Statistics

Stores player performance statistics for matches.

| Column       | Type         | Constraints       | Description                    |
|--------------|--------------|-------------------|--------------------------------|
| player_id    | UUID         | FK, NOT NULL      | Reference to Players table     |
| match_id     | UUID         | FK, NOT NULL      | Reference to Matches table     |
| goals        | INTEGER      | DEFAULT 0         | Goals scored                   |
| assists      | INTEGER      | DEFAULT 0         | Assists provided               |
| yellow_cards | INTEGER      | DEFAULT 0         | Yellow cards received          |
| red_cards    | INTEGER      | DEFAULT 0         | Red cards received             |
| minutes      | INTEGER      | DEFAULT 0         | Minutes played                 |
| rating       | DECIMAL(3,1) |                   | Performance rating (0-10)      |

### Chat_Rooms

Stores chat room information.

| Column      | Type         | Constraints       | Description                    |
|-------------|--------------|-------------------|--------------------------------|
| id          | UUID         | PK                | Unique identifier              |
| name        | VARCHAR(100) | NOT NULL          | Chat room name                 |
| description | TEXT         |                   | Chat room description          |
| type        | VARCHAR(20)  | NOT NULL          | Room type (MATCH, GENERAL)     |
| created_at  | TIMESTAMP    | NOT NULL          | Record creation timestamp      |
| updated_at  | TIMESTAMP    | NOT NULL          | Last update timestamp          |

### Messages

Stores chat messages.

| Column       | Type         | Constraints       | Description                    |
|--------------|--------------|-------------------|--------------------------------|
| id           | UUID         | PK                | Unique identifier              |
| chat_room_id | UUID         | FK, NOT NULL      | Reference to Chat_Rooms table  |
| user_id      | UUID         | FK, NOT NULL      | Reference to Users table       |
| content      | TEXT         | NOT NULL          | Message content                |
| type         | VARCHAR(20)  | NOT NULL          | Message type (TEXT, IMAGE)     |
| created_at   | TIMESTAMP    | NOT NULL          | Message timestamp              |

## Indexes

| Table         | Columns                    | Type    | Description                                |
|---------------|----------------------------|---------|-------------------------------------------|
| Users         | email                      | BTREE   | Fast lookup by email                      |
| Users         | username                   | BTREE   | Fast lookup by username                   |
| Teams         | name                       | BTREE   | Fast lookup by team name                  |
| Players       | team_id                    | BTREE   | Fast lookup of players by team            |
| Matches       | home_team_id, away_team_id | BTREE   | Fast lookup of matches by teams           |
| Matches       | start_time                 | BTREE   | Fast lookup of matches by date            |
| Matches       | status                     | BTREE   | Fast lookup of matches by status          |
| Match_Events  | match_id                   | BTREE   | Fast lookup of events by match            |
| Match_Stats   | match_id                   | BTREE   | Fast lookup of statistics by match        |
| Messages      | chat_room_id               | BTREE   | Fast lookup of messages by chat room      |
| Messages      | created_at                 | BTREE   | Fast lookup of messages by timestamp      |

## Redis Schema

Redis is used for caching and real-time data management:

### Cache Keys

| Key Pattern                   | Type   | Description                               | TTL      |
|-------------------------------|--------|-------------------------------------------|----------|
| `user:{id}`                   | Hash   | User profile data                         | 1 hour   |
| `match:{id}`                  | Hash   | Match data                                | 5 min    |
| `match:{id}:events`           | List   | Match events                              | 5 min    |
| `match:{id}:stats`            | Hash   | Match statistics                          | 5 min    |
| `team:{id}`                   | Hash   | Team data                                 | 1 hour   |
| `team:{id}:players`           | List   | Team players                              | 1 hour   |

### WebSocket Session Management

| Key Pattern                   | Type   | Description                               | TTL      |
|-------------------------------|--------|-------------------------------------------|----------|
| `ws:sessions:{userId}`        | Set    | User's active WebSocket sessions          | Session  |
| `ws:subscriptions:{topic}`    | Set    | Users subscribed to a topic               | Session  |
| `ws:presence:{chatRoomId}`    | Hash   | Users present in a chat room              | Session  |

### Rate Limiting

| Key Pattern                   | Type   | Description                               | TTL      |
|-------------------------------|--------|-------------------------------------------|----------|
| `ratelimit:api:{userId}`      | String | API rate limit counter                    | 1 min    |
| `ratelimit:ws:{userId}`       | String | WebSocket connection counter              | Session  |

## Database Migration Strategy

The application uses Flyway for database migrations:

1. Versioned migrations in `src/main/resources/db/migration`
2. Naming convention: `V{version}__{description}.sql`
3. Migrations are applied automatically on application startup

## Backup Strategy

1. Daily full database backups
2. Point-in-time recovery with WAL (Write-Ahead Logging)
3. Backup retention: 30 days

## Data Access Layer

In accordance with hexagonal architecture:

1. Repository interfaces (ports) are defined in the application layer
2. PostgreSQL implementations (adapters) are in the infrastructure layer
3. Redis cache implementations (adapters) are in the infrastructure layer

This separation ensures that the core domain is not dependent on specific database technologies.
