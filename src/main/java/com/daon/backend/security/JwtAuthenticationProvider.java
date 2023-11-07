package com.daon.backend.security;

import com.daon.backend.auth.domain.JwtManager;
import com.daon.backend.auth.domain.Payload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtAuthenticationProvider {

    private final JwtManager jwtManager;

    protected Authentication authenticateAccessToken(String accessToken) {
        if (!jwtManager.validate(accessToken)) {
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
