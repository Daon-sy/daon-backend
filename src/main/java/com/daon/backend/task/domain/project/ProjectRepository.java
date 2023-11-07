package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);

    Optional<Project> findProjectByIdAndWorkspaceId(Long projectId, Long workspaceId);
}
