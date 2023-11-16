package com.daon.backend.member.infrastructure;

import com.daon.backend.member.domain.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, String> {

    @EntityGraph(attributePaths = "emails")
    Optional<Member> findByIdAndRemovedFalse(String memberId);

    @EntityGraph(attributePaths = "emails")
    Optional<Member> findByUsernameAndRemovedFalse(String username);
}
