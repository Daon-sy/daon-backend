package com.daon.backend.member.domain;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Optional<Member> findByEmail(String email);
}
