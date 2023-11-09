package com.daon.backend.auth.infrastructure;

import com.daon.backend.auth.domain.*;
import com.daon.backend.security.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtManagerImpl implements JwtManager {

    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return false;
        }

        return true;
    }

    @Override
    public Token createMemberAccessToken(String memberId) {
        log.debug("create access-token");
        return buildAccessToken(Role.MEMBER, memberId);
    }

    @Override
    public Token createMemberRefreshToken(String memberId) {
        Token refreshToken = buildRefreshToken();
        refreshTokenRepository.add(refreshToken.getValue(), memberId, refreshToken.getExpiredAt());
        log.debug("create refresh-token");
        return refreshToken;
    }

    @Override
    public Payload parse(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new Payload(
                claims.getSubject(),
                claims.get("role", String.class),
                claims.getIssuedAt().toInstant(),
                claims.getExpiration().toInstant()
        );
    }

    @Override
    public Tokens issueTokens(String memberId) {
        Token accessToken = createMemberAccessToken(memberId);
        Token refreshToken = createMemberRefreshToken(memberId);
        return new Tokens(accessToken, refreshToken);
    }

    @Override
    public Tokens reissueTokens(String refreshTokenValue) {
        if (!validate(refreshTokenValue)) {
            throw new InvalidRefreshTokenException();
        }

        String memberId = refreshTokenRepository.findMemberIdByRtk(refreshTokenValue)
                .orElseThrow(UnauthenticatedMemberException::new);

        Token accessToken = createMemberAccessToken(memberId);
        Payload rtkPayload = parse(refreshTokenValue);
        long remainSec = Duration.between(Instant.now(), rtkPayload.getExpiredAt()).toSeconds();
        Token refreshToken = null;
        if (remainSec <= jwtProperties.getRefreshTokenReissueCondSec()) {
            refreshToken = createMemberRefreshToken(memberId);
        }

        return new Tokens(accessToken, refreshToken);
    }

    private Token buildAccessToken(Role role, String id) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getAccessTokenExpSec());
        String tokenValue = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .claim("role", role.getValue())
                .setSubject(id)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .compact();

        return new Token(tokenValue, now, expiration);
    }

    private Token buildRefreshToken() {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(jwtProperties.getRefreshTokenExpSec());
        String tokenValue = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .compact();

        return new Token(tokenValue, now, expiration);
    }
}
