package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.workspace.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.task.QTask.task;
import static com.daon.backend.task.domain.workspace.QMessage.message;
import static com.daon.backend.task.domain.workspace.QWorkspace.workspace;
import static com.daon.backend.task.domain.workspace.QWorkspaceParticipant.workspaceParticipant;

@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJpaRepository workspaceJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Workspace save(Workspace workspace) {
        return workspaceJpaRepository.save(workspace);
    }

    @Override
    public Optional<Workspace> findById(Long workspaceId) {
        return workspaceJpaRepository.findWorkspaceById(workspaceId);
    }

    @Override
    public List<Workspace> findWorkspacesByMemberId(String memberId) {
        return queryFactory
                .selectFrom(workspace)
                .innerJoin(workspace.participants, workspaceParticipant)
                .where(workspaceParticipant.memberId.eq(memberId))
                .orderBy(
                        workspace.division.desc(),
                        workspace.title.asc()
                )
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
    public Page<Message> findSendMessages(Workspace workspace, Long senderId, String target, String keyword, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        if (target.equals("title")) {
            builder.and(message.title.containsIgnoreCase(keyword));
        }

        List<Message> messages = queryFactory
                .selectFrom(message)
                .where(builder.and(message.workspace.id.eq(workspace.getId()))
                        .and(message.senderId.eq(senderId)))
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
    public void deleteById(Long workspaceId) {
        workspaceJpaRepository.deleteById(workspaceId);
    }

    @Override
    public void deleteTaskManager(Long workspaceParticipantId) {
        workspaceJpaRepository.deleteTaskManager(workspaceParticipantId);
    }

    @Override
    public void deleteMessages(Long workspaceParticipantId) {
        queryFactory
                .delete(message)
                .where(message.receiverId.eq(workspaceParticipantId))
                .execute();
    }
}
