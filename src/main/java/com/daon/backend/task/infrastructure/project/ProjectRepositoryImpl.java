package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.ProjectRepository;
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
    public Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId) {
        return projectParticipantJpaRepository.findProjectParticipantByProjectIdAndMemberId(projectId, memberId);
    }

    @Override
    public List<Project> findProjectsByMemberIdOrderByDesc(String memberId) {
        return queryFactory
                .selectFrom(project)
                .innerJoin(project.participants, projectParticipant)
                .where(projectParticipant.memberId.eq(memberId))
                .orderBy(project.createdAt.desc())
                .fetch();
    }

    @Override
    public List<ProjectParticipant> findProjectParticipantsByProjectId(Long projectId) {
        return projectParticipantJpaRepository.findProjectParticipantsByProjectIdOrderByCreatedAtDesc(projectId);
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
    }

    @Override
    public void deleteTasksAndBoardsRelatedProject(Long projectId) {
        queryFactory
                .update(task)
                .set(task.taskManager, (ProjectParticipant) null)
                .set(task.removed, true)
                .where(task.project.id.eq(projectId))
                .execute();

        queryFactory
                .update(board)
                .set(board.removed, true)
                .where(board.project.id.eq(projectId))
                .execute();

        em.flush();
    }
}
