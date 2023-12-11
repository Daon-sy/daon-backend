package com.daon.backend.member.domain;

import com.daon.backend.member.dto.MemberSettingsResponseDto;
import com.daon.backend.member.dto.MemberSummary;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByUsername(String username);

    Optional<Member> findById(String memberId);

    boolean existsByEmail(String email);

    Optional<MemberSettingsResponseDto> findSettingsByMemberId(String memberId);
    List<MemberSummary> searchMembersByUsername(String username);
}
