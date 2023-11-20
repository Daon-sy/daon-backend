package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.dto.ProjectSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    Optional<Project> findProjectByProjectId(Long projectId);

    Optional<Project> findProjectWithParticipantsById(Long projectId);

    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId);

    List<ProjectParticipant> findProjectParticipantsWithWorkspaceParticipantsByProjectId(Long projectId);

    List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);

    List<Project> findAllProjectsByWorkspaceId(Long workspaceId);

    List<Project> findAllProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);

    List<Project> findProjectsByWorkspaceParticipantId(Long workspaceParticipantId);

    Optional<Project> findProjectWithBoardsByProjectId(Long projectId);

    Slice<ProjectSummary> searchProjectSummariesByTitle(String memberId, String title, Pageable pageable);
}
