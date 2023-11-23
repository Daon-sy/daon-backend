package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.dto.ProjectSummary;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.board.QBoard.board;
import static com.daon.backend.task.domain.project.QProject.project;
import static com.daon.backend.task.domain.project.QProjectParticipant.projectParticipant;
import static com.daon.backend.task.domain.task.QTask.task;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;
    private final ProjectParticipantJpaRepository projectParticipantJpaRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }

    @Override
    public Optional<Project> findProjectById(Long projectId) {
        return projectJpaRepository.findProjectByIdAndRemovedFalse(projectId);
    }

    @Override
    public Optional<Project> findProjectWithParticipantsByProjectId(Long projectId) {
        return projectJpaRepository.findProjectWithParticipantsByIdAndRemovedFalse(projectId);
    }

    @Override
    public Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId) {
        return projectParticipantJpaRepository.findProjectParticipantByProjectIdAndMemberId(projectId, memberId);
    }

    @Override
    public List<ProjectParticipant> findProjectParticipantsWithWorkspaceParticipantsByProjectId(Long projectId) {
        return projectParticipantJpaRepository.findProjectParticipantsWithWorkspaceParticipantsByProjectId(projectId);
    }

    @Override
    public List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant) {
        return projectParticipantJpaRepository.findProjectsByWorkspaceParticipantAndRemovedFalse(workspaceParticipant);
    }

    @Override
    public List<Project> findAllProjectsByWorkspaceId(Long workspaceId) {
        return projectJpaRepository.findAllProjectsByWorkspaceId(workspaceId);
    }

    @Override
    public List<Project> findAllProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant) {
        return projectParticipantJpaRepository.findProjectsByWorkspaceParticipant(workspaceParticipant);
    }

    @Override
    public List<Project> findProjectsByWorkspaceParticipantId(Long workspaceParticipantId) {
        return queryFactory
                .selectFrom(project)
                .innerJoin(project.participants, projectParticipant)
                .where(projectParticipant.workspaceParticipant.id.eq(workspaceParticipantId))
                .fetch();
    }

    @Override
    public Optional<Project> findProjectWithBoardsByProjectId(Long projectId) {
        return projectJpaRepository.findProjectWithBoardsByIdAndRemovedFalse(projectId);
    }

    @Override
    public Optional<Project> findProjectWithTasksByProjectId(Long projectId) {
        return projectJpaRepository.findProjectWithTasksByIdAndRemovedFalse(projectId);
    }

    @Override
    public Slice<ProjectSummary> searchProjectSummariesByTitle(String memberId, String title, Pageable pageable) {
        final int pageSize = pageable.getPageSize();
        final long offset = pageable.getOffset();

        List<ProjectSummary> projectSummaries = queryFactory
                .select(
                        Projections.constructor(
                                ProjectSummary.class,
                                project.id,
                                project.title,
                                project.description
                        )
                )
                .from(project)
                .innerJoin(project.participants, projectParticipant)
                .where(project.title.contains(title)
                        .and(projectParticipant.memberId.eq(memberId))
                        .and(project.removed.isFalse()))
                .orderBy(project.modifiedAt.desc())
                .offset(offset)
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = false;
        if (projectSummaries.size() > pageSize) {
            projectSummaries.remove(pageSize);
            hasNext = true;
        }

        return new SliceImpl<>(projectSummaries, pageable, hasNext);
    }

    @Override
    public void deleteTaskManagerRelatedProjectByMemberId(Long projectId, String memberId) {
        projectJpaRepository.deleteTaskManagerRelatedProjectByMemberId(projectId, memberId);
    }

    @Override
    public void deleteTaskManagerByProjectParticipantId(Long projectParticipantId) {
        queryFactory
                .update(task)
                .set(task.taskManager, (ProjectParticipant) null)
                .where(task.taskManager.id.eq(projectParticipantId))
                .execute();

        em.flush();
        em.clear();
    }

    @Override
    public void deleteTasksAndBoardsRelatedProject(Long projectId) {
        queryFactory
                .update(task)
                .set(task.taskManager, (ProjectParticipant) null)
                .set(task.creatorId, (Long) null)
                .set(task.removed, true)
                .where(task.project.id.eq(projectId))
                .execute();

        queryFactory
                .update(board)
                .set(board.removed, true)
                .where(board.project.id.eq(projectId))
                .execute();

        em.flush();
        em.clear();
    }
}
