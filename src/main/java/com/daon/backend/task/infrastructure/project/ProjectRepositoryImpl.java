package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
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
    public Optional<Project> findProjectById(Long projectId) {
        return projectJpaRepository.findProjectById(projectId);
    }

    @Override
    public Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId) {
        return projectParticipantJpaRepository.findProjectParticipantByProjectIdAndMemberId(projectId, memberId);
    }

    @Override
    public List<Project> findProjectsByMemberId(Long workspaceId, String memberId) {
        return queryFactory
                .selectFrom(project)
                .innerJoin(project.participants, projectParticipant)
                .where(projectParticipant.memberId.eq(memberId)
                        .and(project.workspace.id.eq(workspaceId)))
                .orderBy(project.createdAt.asc())
                .fetch();
    }

    @Override
    public List<ProjectParticipant> findProjectParticipantsByProjectId(Long projectId) {
        return queryFactory
                .selectFrom(projectParticipant)
                .where(projectParticipant.project.id.eq(projectId))
                .orderBy(
                        projectParticipant.workspaceParticipant.role.desc(),
                        projectParticipant.workspaceParticipant.profile.name.asc()
                )
                .fetch();
    }

    @Override
    public void deleteTaskManager(Long projectParticipantId) {
        projectJpaRepository.deleteTaskManager(projectParticipantId);
    }
}
