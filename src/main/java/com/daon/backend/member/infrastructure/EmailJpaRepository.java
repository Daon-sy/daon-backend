package com.daon.backend.member.infrastructure;

import com.daon.backend.member.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailJpaRepository extends JpaRepository<Email, Long> {

    boolean existsEmailByEmail(String email);
}
