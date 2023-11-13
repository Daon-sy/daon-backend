package com.daon.backend.member.domain;

import com.daon.backend.member.dto.ModifyMemberDto;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {

    void save(Member member);

    Optional<Member> findByEmail(String email);

    Optional<Member> findById(UUID memberId);

}
