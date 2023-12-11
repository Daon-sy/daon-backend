package com.daon.backend.member.infrastructure;

import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.dto.MemberSettingsResponseDto;
import com.daon.backend.member.dto.MemberSummary;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
        return memberJpaRepository.findByUsernameAndRemovedFalse(username);
    }

    @Override
    public Optional<Member> findById(String memberId) {
        return memberJpaRepository.findByIdAndRemovedFalse(memberId);
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
    public List<MemberSummary> searchMembersByUsername(String username) {
        return queryFactory
                .select(
                        Projections.constructor(
                                MemberSummary.class,
                                member.username,
                                member.name
                        )
                )
                .from(member)
                .where(member.username.contains(username)
                        .and(member.removed.isFalse()))
                .fetch();
    }

}
