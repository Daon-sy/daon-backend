package com.daon.backend.config;

import com.daon.backend.security.MemberNotAuthenticatedException;
import com.daon.backend.security.SecurityUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public JPAQueryFactory queryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            try {
                return Optional.of(SecurityUtils.getMemberPrincipal().getMemberId());
            } catch (MemberNotAuthenticatedException e) {
                return Optional.empty();
            }
        };
    }
}
