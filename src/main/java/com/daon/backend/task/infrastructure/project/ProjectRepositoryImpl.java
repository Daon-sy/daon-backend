package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {

    private final ProjectJpaRepository projectJpaRepository;
    private final ProjectParticipantJpaRepository projectParticipantJpaRepository;

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
}
