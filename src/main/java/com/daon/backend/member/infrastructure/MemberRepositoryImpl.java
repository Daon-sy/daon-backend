package com.daon.backend.member.infrastructure;

import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.dto.MemberSettingsResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.daon.backend.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final EmailJpaRepository emailJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberJpaRepository.findMemberById(memberId);
    }

    @Override
    public boolean existsByEmail(String email) {
        return emailJpaRepository.existsEmailByEmail(email);
    }

    @Override
    public Optional<MemberSettingsResponseDto> findSettingsByMemberId(String memberId) {
        return Optional.ofNullable(
                queryFactory
                        .select(
                                Projections.constructor(
                                        MemberSettingsResponseDto.class,
                                        member.settings.notified
                                )
                        )
                        .from(member)
                        .where(member.id.eq(memberId))
                        .fetchOne()
        );
    }

    @Override
    public void deleteMemberById(String memberId) {
        memberJpaRepository.deleteById(memberId);
    }
}
