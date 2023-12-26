package com.daon.backend.security;

import com.daon.backend.auth.domain.JwtManager;
import com.daon.backend.auth.domain.Payload;
import com.daon.backend.auth.domain.UnauthenticatedMemberException;
import com.daon.backend.common.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtAuthenticationProvider {

    private final JwtManager jwtManager;
    private final RedisRepository redisRepository;

    protected Authentication authenticateAccessToken(String accessToken) {
        if (!jwtManager.validate(accessToken)) {
            return null;
        }

        if (StringUtils.hasText(redisRepository.get(accessToken))) {
            return null;
        }

        Payload payload = jwtManager.parse(accessToken);
        String memberId = payload.getId();
        String role = payload.getRole();

        return new UsernamePasswordAuthenticationToken(
                new MemberPrincipal(memberId),
                null,
                Collections.singleton(new SimpleGrantedAuthority(role))
        );
    }
}
