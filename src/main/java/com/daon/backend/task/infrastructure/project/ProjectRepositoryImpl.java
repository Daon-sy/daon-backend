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

import java.util.List;
import java.util.Optional;

import static com.daon.backend.task.domain.project.QProject.project;
import static com.daon.backend.task.domain.project.QProjectParticipant.projectParticipant;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;
    private final ProjectParticipantJpaRepository projectParticipantJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Project save(Project project) {
        return projectJpaRepository.save(project);
    }

    @Override
    public Optional<Project> findProjectByProjectId(Long projectId) {
        return projectJpaRepository.findById(projectId);
    }

    @Override
    public Optional<Project> findProjectWithParticipantsById(Long projectId) {
        return projectJpaRepository.findProjectWithParticipantsById(projectId);
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
        return projectParticipantJpaRepository.findProjectsByWorkspaceParticipant(workspaceParticipant);
    }

    @Override
    public Optional<Project> findProjectWithBoardsByProjectId(Long projectId) {
        return projectJpaRepository.findProjectWithBoardsById(projectId);
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
                        .and(projectParticipant.memberId.eq(memberId)))
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
}
