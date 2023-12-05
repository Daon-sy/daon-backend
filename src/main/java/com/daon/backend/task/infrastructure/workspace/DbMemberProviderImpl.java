package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.task.dto.workspace.SearchMemberResponseDto;
import com.daon.backend.task.service.DbMemberProvider;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.daon.backend.member.domain.QMember.member;
import static com.daon.backend.task.domain.workspace.QWorkspaceInvitation.workspaceInvitation;
import static com.daon.backend.task.domain.workspace.QWorkspaceParticipant.workspaceParticipant;

@RequiredArgsConstructor
@Service
public class DbMemberProviderImpl implements DbMemberProvider {

    private final MemberRepository memberRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public String getMemberIdByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> MemberNotFoundException.byUsername(username))
                .getId();
    }

    @Override
    public List<SearchMemberResponseDto.MemberSummary> searchMemberByUsername(String username, Long workspaceId) {

        return queryFactory
                .select(
                        Projections.constructor(
                                SearchMemberResponseDto.MemberSummary.class,
                                member.username,
                                member.name,
                                workspaceInvitation.isNotNull()
                        )
                )
                .from(member)
                .leftJoin(workspaceParticipant)
                .on(workspaceParticipant.workspace.id.eq(workspaceId).and(member.id.eq(workspaceParticipant.memberId)))
                .leftJoin(workspaceInvitation)
                .on(workspaceInvitation.workspace.id.eq(workspaceId))
                .where(member.username.contains(username)
                        .and(member.removed.isFalse())
                        .and(workspaceParticipant.id.isNull()))
                .fetch();
    }
}
