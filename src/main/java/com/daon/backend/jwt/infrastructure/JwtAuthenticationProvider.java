package com.daon.backend.jwt.infrastructure;

import com.daon.backend.jwt.domain.JwtService;
import com.daon.backend.jwt.domain.Payload;
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

    private final JwtService jwtService;

    protected Authentication authenticateAccessToken(String accessToken) {
        if (!jwtService.validate(accessToken)) {
            log.info("is invalid token");
            return null;
        }

        Payload payload = jwtService.parse(accessToken);
        String id = payload.getId();
        String role = payload.getRole();

        return new UsernamePasswordAuthenticationToken(id, null, Collections.singleton(new SimpleGrantedAuthority(role)));
    }
}
