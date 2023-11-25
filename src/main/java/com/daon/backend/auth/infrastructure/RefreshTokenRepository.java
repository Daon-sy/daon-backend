package com.daon.backend.auth.infrastructure;

import com.daon.backend.common.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private final RedisRepository redisRepository;

    public void add(String refreshToken, String memberId, Instant expiry) {
        redisRepository.set(refreshToken, memberId, Duration.between(Instant.now(), expiry));
    }

    public Optional<String> findMemberIdByRtk(String rtk) {
        String memberId = redisRepository.get(rtk);
        if (memberId == null) {
            return Optional.empty();
        }
        return Optional.of(memberId);
    }

    public void prohibitAccessToken(String accessToken, String refreshToken, String message, Instant expiry) {
        redisRepository.remove(refreshToken);
        redisRepository.set(accessToken, message, Duration.between(Instant.now(), expiry));
    }
}
