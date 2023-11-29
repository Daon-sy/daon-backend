package com.daon.backend.member.domain;

import com.daon.backend.member.dto.MemberSummary;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Optional<Member> findByUsername(String username);

    Optional<Member> findById(String memberId);

    boolean existsByEmail(String email);

    List<MemberSummary> searchMembersByUsername(String username);
}
