package com.daon.backend.task.domain.project;

import com.daon.backend.task.dto.ProjectSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    Optional<Project> findProjectById(Long projectId);

    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId);

    List<Project> findProjectsByMemberId(Long workspaceId, String memberId);

    List<ProjectParticipant> findProjectParticipantsByProjectId(Long projectId);

    Slice<ProjectSummary> searchProjectSummariesByTitle(String memberId, String title, Pageable pageable);

    void deleteTaskManagerRelatedProjectByMemberId(Long projectId, String memberId);

    void deleteTaskManagerByProjectParticipantId(Long projectParticipantId);

    void deleteTasksAndBoardsRelatedProject(Long projectId);

    void deleteAllTaskBookmarkRelatedProjectParticipant(Long projectParticipantId);
}
