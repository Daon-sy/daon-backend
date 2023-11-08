package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    @EntityGraph("participants")
    Optional<Project> findProjectWithParticipantsById(Long projectId);

    List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);

    Optional<ProjectParticipant> findProjectParticipantByProjectAndMemberId(Project project, String memberId);

    Optional<Project> findProjectByIdAndWorkspaceId(Long projectId, Long workspaceId);
}
