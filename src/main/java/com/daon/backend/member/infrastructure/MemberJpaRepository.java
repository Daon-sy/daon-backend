package com.daon.backend.member.infrastructure;

import com.daon.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberJpaRepository extends JpaRepository<Member, UUID> {

    Optional<Member> findByEmail(String email);
}
