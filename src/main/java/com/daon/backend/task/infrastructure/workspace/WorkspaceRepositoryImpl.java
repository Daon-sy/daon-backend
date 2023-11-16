package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.WorkspaceSummary;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.workspace.QWorkspace.workspace;
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
    public List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId) {
        return workspaceParticipantJpaRepository.findWorkspaceParticipantsByWorkspaceId(workspaceId);
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

    @Override
    public Slice<WorkspaceSummary> searchWorkspaceSummariesByTitle(String memberId, String title, Pageable pageable) {
        final int pageSize = pageable.getPageSize();
        final long offset = pageable.getOffset();

        List<WorkspaceSummary> workspaceSummaries = jpaQueryFactory
                .select(
                        Projections.constructor(
                                WorkspaceSummary.class,
                                workspace.id,
                                workspace.title,
                                workspace.imageUrl,
                                workspace.division,
                                workspace.description
                        )
                )
                .from(workspace)
                .innerJoin(workspace.participants, workspaceParticipant)
                .where(workspace.title.contains(title)
                        .and(workspaceParticipant.memberId.eq(memberId)))
                .orderBy(workspace.modifiedAt.desc())
                .offset(offset)
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if (workspaceSummaries.size() > pageSize) {
            workspaceSummaries.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(workspaceSummaries, pageable, hasNext);
    }
}
