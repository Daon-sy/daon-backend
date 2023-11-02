package com.daon.backend.jwt.infrastructure;

import com.daon.backend.jwt.domain.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtAuthenticationProvider {

    private final JwtService jwtService;

    private static final String BEARER_TYPE = "Bearer ";

    protected String getValidAuthentication(HttpServletRequest request) {
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (!StringUtils.hasText(accessToken)) {
            return null;
        }

        String token = accessToken.substring(BEARER_TYPE.length());
        if (!jwtService.validate(token) || accessToken.startsWith(BEARER_TYPE)) {
            log.info("is invalid token");
            return null;
        }

        return token;
    }
}
