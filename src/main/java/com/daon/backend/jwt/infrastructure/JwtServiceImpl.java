package com.daon.backend.jwt.infrastructure;

import com.daon.backend.security.Role;
import com.daon.backend.jwt.domain.JwtService;
import com.daon.backend.jwt.domain.Payload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Override
    public boolean validate(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.info("{}", e.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public String createMemberAccessToken(String memberId) {
        return buildJwt(Role.MEMBER, memberId);
    }

    @Override
    public String createAdminAccessToken(String adminId) {
        return buildJwt(Role.ADMIN, adminId);
    }

    @Override
    public Payload parse(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return new Payload(claims.getSubject(), claims.get("role", String.class));
    }

    private String buildJwt(Role role, String id) {
        Instant now = Instant.now();

        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .claim("role", role.getValue())
                .setSubject(id)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(accessTokenExpiration)))
                .compact();
    }
}
