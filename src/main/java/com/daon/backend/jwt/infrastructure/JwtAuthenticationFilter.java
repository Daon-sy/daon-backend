package com.daon.backend.jwt.infrastructure;

import com.daon.backend.jwt.domain.JwtService;
import com.daon.backend.jwt.domain.Payload;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtAuthenticationProvider.getValidAuthentication(request);

        if (token != null) {
            Payload payload = jwtService.parse(token);
            String id = payload.getId();
            String role = payload.getRole();

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            id,
                            null,
                            Collections.singleton(new SimpleGrantedAuthority(role))
                    )
            );
        }

        filterChain.doFilter(request, response);
    }
}
