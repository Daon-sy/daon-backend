package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.dto.ProjectSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    Optional<Project> findById(Long projectId);

    Optional<Project> findProjectWithParticipantsByProjectId(Long projectId);

    Optional<Project> findProjectWithBoardsByProjectId(Long projectId);

    Optional<Project> findProjectWithTasksByProjectId(Long projectId);

    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId);

    List<ProjectParticipant> findProjectParticipantsWithWorkspaceParticipantsByProjectId(Long projectId);

    List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);

    List<Project> findAllProjectsByWorkspaceId(Long workspaceId);

    List<Project> findAllProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);

    List<Project> findProjectsByWorkspaceParticipantId(Long workspaceParticipantId);

    Slice<ProjectSummary> searchProjectSummariesByTitle(String memberId, String title, Pageable pageable);

    void deleteTaskManagerRelatedProjectByMemberId(Long projectId, String memberId);

    void deleteTaskManagerByProjectParticipantId(Long projectParticipantId);

    void deleteTasksRelatedProject(Long projectId);

    void deleteBoardsRelatedProject(Long projectId);
}
