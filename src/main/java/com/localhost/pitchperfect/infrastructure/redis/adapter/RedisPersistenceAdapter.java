package com.localhost.pitchperfect.infrastructure.redis.adapter;

import com.localhost.pitchperfect.application.dto.PresenceStatusDto;
import com.localhost.pitchperfect.application.port.out.PresencePersistencePort;
import com.localhost.pitchperfect.infrastructure.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis-based implementation of the PresencePersistencePort.
 * Manages user presence data in Redis.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPersistenceAdapter implements PresencePersistencePort {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserJpaRepository userRepository;
    
    // Redis key prefixes
    private static final String USER_STATUS_KEY = "user:status:";
    private static final String USER_SUBSCRIPTION_KEY = "user:subscription:";
    
    // TTL for presence data (24 hours)
    private static final long STATUS_TTL = 24 * 60 * 60;

    @Override
    public void saveUserStatus(String userId, PresenceStatusDto status) {
        String key = USER_STATUS_KEY + userId;
        redisTemplate.opsForValue().set(key, status);
        redisTemplate.expire(key, STATUS_TTL, TimeUnit.SECONDS);
        log.debug("Saved user status in Redis: {}", userId);
    }

    @Override
    public String getUsernameById(String userId) {
        // First try to get from Redis cache
        String cacheKey = "user:name:" + userId;
        String username = (String) redisTemplate.opsForValue().get(cacheKey);
        
        if (username == null) {
            // If not in cache, get from database
            username = userRepository.findById(userId)
                    .map(user -> user.getUsername())
                    .orElse("Unknown User");
            
            // Cache the result
            if (!"Unknown User".equals(username)) {
                redisTemplate.opsForValue().set(cacheKey, username);
                redisTemplate.expire(cacheKey, STATUS_TTL, TimeUnit.SECONDS);
            }
        }
        
        return username;
    }

    @Override
    public void saveUserSubscription(String userId, String subscriptionId, String roomId) {
        String key = USER_SUBSCRIPTION_KEY + userId + ":" + subscriptionId;
        redisTemplate.opsForValue().set(key, roomId);
        redisTemplate.expire(key, STATUS_TTL, TimeUnit.SECONDS);
        log.debug("Saved user subscription in Redis: {} -> {}", subscriptionId, roomId);
    }

    @Override
    public void removeUserSubscription(String userId, String subscriptionId) {
        String key = USER_SUBSCRIPTION_KEY + userId + ":" + subscriptionId;
        redisTemplate.delete(key);
        log.debug("Removed user subscription from Redis: {}", subscriptionId);
    }

    @Override
    public void removeAllUserSubscriptions(String userId) {
        // Find all keys matching the pattern
        String pattern = USER_SUBSCRIPTION_KEY + userId + ":*";
        redisTemplate.keys(pattern).forEach(key -> {
            redisTemplate.delete(key);
        });
        log.debug("Removed all subscriptions for user: {}", userId);
    }

    @Override
    public String getRoomIdBySubscription(String userId, String subscriptionId) {
        String key = USER_SUBSCRIPTION_KEY + userId + ":" + subscriptionId;
        return (String) redisTemplate.opsForValue().get(key);
    }
}
