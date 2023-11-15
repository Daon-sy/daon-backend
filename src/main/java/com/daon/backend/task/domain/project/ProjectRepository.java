package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    Optional<Project> findProjectByProjectId(Long projectId);

    Optional<Project> findProjectWithParticipantsById(Long projectId);

    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId);

    List<ProjectParticipant> findProjectParticipantsWithWorkspaceParticipantsByProjectId(Long projectId);

    List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);

    Optional<Project> findProjectWithBoardsByProjectId(Long projectId);
}
