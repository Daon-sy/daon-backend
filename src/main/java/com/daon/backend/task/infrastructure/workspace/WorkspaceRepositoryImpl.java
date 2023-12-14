package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.WorkspaceSummary;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.workspace.QMessage.message;
import static com.daon.backend.task.domain.workspace.QWorkspace.workspace;
import static com.daon.backend.task.domain.workspace.QWorkspaceParticipant.workspaceParticipant;

@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJpaRepository workspaceJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Workspace save(Workspace workspace) {
        return workspaceJpaRepository.save(workspace);
    }

    @Override
    public Optional<Workspace> findWorkspaceById(Long workspaceId) {
        return workspaceJpaRepository.findWorkspaceByIdAndRemovedFalse(workspaceId);
    }

    @Override
    public List<Workspace> findWorkspacesByMemberId(String memberId) {
        return queryFactory
                .selectFrom(workspace)
                .innerJoin(workspace.participants, workspaceParticipant)
                .where(workspaceParticipant.memberId.eq(memberId))
                .orderBy(workspace.createdAt.asc())
                .fetch();
    }

    @Override
    public List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId) {
        return queryFactory
                .selectFrom(workspaceParticipant)
                .where(workspaceParticipant.workspace.id.eq(workspaceId))
                .orderBy(
                        workspaceParticipant.role.desc(),
                        workspaceParticipant.profile.name.asc()
                )
                .fetch();
    }

    @Override
    public Role findParticipantRoleByMemberIdAndWorkspaceId(String memberId, Long workspaceId) {
        return queryFactory
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

        List<WorkspaceSummary> workspaceSummaries = queryFactory
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
                        .and(workspaceParticipant.memberId.eq(memberId))
                        .and(workspace.removed.isFalse()))
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

    @Override
    public void deleteAllRelatedWorkspace(Long workspaceId) {
        workspaceJpaRepository.deleteTaskRepliesRelatedWorkspace(workspaceId);
        workspaceJpaRepository.deleteTasksRelatedWorkspace(workspaceId);
        workspaceJpaRepository.deleteBoardsRelatedWorkspace(workspaceId);
        workspaceJpaRepository.deleteProjectsRelatedWorkspace(workspaceId);
    }

    @Override
    public void deleteAllRelatedWorkspaceParticipant(Long workspaceParticipantId, String memberId) {
        workspaceJpaRepository.deleteTaskManagerRelatedWorkspaceParticipant(workspaceParticipantId);
    }

    @Override
    public Page<Message> findMessages(Workspace workspace, Long receiverId, String target, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (target.equals("title")) {
            builder.and(message.title.containsIgnoreCase(keyword));
        } else if (target.equals("sender")) {
            builder.and(message.senderName.containsIgnoreCase(keyword));
        }

        List<Message> messages = queryFactory
                .selectFrom(message)
                .where(builder.and(message.workspace.id.eq(workspace.getId()))
                        .and(message.receiverId.eq(receiverId)))
                .orderBy(message.createdAt.desc())
                .fetch();

        return new PageImpl<>(messages, pageable, messages.size());
    }

    @Override
    public void readAllMessages(Long workspaceId, Long receiverId) {
        queryFactory
                .update(message)
                .set(message.readed, true)
                .where(message.workspace.id.eq(workspaceId)
                        .and(message.receiverId.eq(receiverId)))
                .execute();
    }

    @Override
    public void deleteAllMessagesRelatedWorkspaceParticipant(Long workspaceParticipantId) {
        queryFactory
                .delete(message)
                .where(message.receiverId.eq(workspaceParticipantId))
                .execute();
    }

    @Override
    public void deleteAllReplyWriterRelatedMemberId(Long workspaceParticipantId, String memberId) {
        workspaceJpaRepository.deleteAllReplyWriterRelatedMemberId(workspaceParticipantId, memberId);
    }

}
