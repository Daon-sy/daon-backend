package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.workspace.QWorkspaceParticipant.workspaceParticipant;

@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJpaRepository workspaceJpaRepository;
    private final WorkspaceParticipantJpaRepository workspaceParticipantJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Workspace save(Workspace workspace) {
        return workspaceJpaRepository.save(workspace);
    }

    @Override
    public Optional<Workspace> findWorkspaceByWorkspaceId(Long workspaceId) {
        return workspaceJpaRepository.findById(workspaceId);
    }

    @Override
    public Optional<Workspace> findWorkspaceWithParticipantsByWorkspaceId(Long workspaceId) {
        return workspaceJpaRepository.findWorkspaceWithParticipantsById(workspaceId);
    }

    @Override
    public List<Workspace> findWorkspacesByMemberId(String memberId) {
        // 회원 아이디로
        return workspaceParticipantJpaRepository.findWorkspacesByMemberId(memberId);
    }

    @Override
    public Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceAndMemberId(Workspace workspace, String memberId) {
        return workspaceParticipantJpaRepository.findByWorkspaceAndMemberId(workspace, memberId);
    }

    @Override
    public Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceParticipantId(Long workspaceParticipantId) {
        return workspaceParticipantJpaRepository.findById(workspaceParticipantId);
    }

    @Override
    public Optional<Workspace> findWorkspaceByJoinCode(String joinCode) {
        return workspaceJpaRepository.findWorkspaceByJoinCode(joinCode);
    }

    @Override
    public List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId) {
        return workspaceParticipantJpaRepository.findWorkspaceParticipantsByWorkspaceId(workspaceId);
    }

    @Override
    public boolean existsWorkspaceParticipantByMemberIdAndWorkspaceId(String memberId, Long workspaceId) {
        return workspaceParticipantJpaRepository.existsWorkspaceParticipantByMemberIdAndWorkspaceId(memberId, workspaceId);
    }

    @Override
    public Role findParticipantRoleByMemberIdAndWorkspaceId(String memberId, Long workspaceId) {
        return jpaQueryFactory
                .select(workspaceParticipant.role)
                .from(workspaceParticipant)
                .where(workspaceParticipant.memberId.eq(memberId)
                        .and(workspaceParticipant.workspace.id.eq(workspaceId)))
                .fetchFirst();
    }
}
