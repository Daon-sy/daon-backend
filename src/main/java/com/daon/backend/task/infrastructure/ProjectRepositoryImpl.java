package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
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
    public List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant) {
        return projectParticipantJpaRepository.findProjectsByWorkspaceParticipant(workspaceParticipant);
    }

    @Override
    public Optional<Project> findProjectByIdAndWorkspaceId(Long projectId, Long workspaceId) {
        return projectJpaRepository.findProjectByIdAndWorkspaceId(projectId, workspaceId);
    }
}
