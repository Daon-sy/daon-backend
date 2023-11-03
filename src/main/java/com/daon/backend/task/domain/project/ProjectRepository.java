package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;

import java.util.List;

public interface ProjectRepository {

    Project save(Project project);

    List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);
}
