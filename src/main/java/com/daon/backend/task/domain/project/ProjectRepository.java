package com.daon.backend.task.domain.project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository {

    Project save(Project project);

    Optional<Project> findProjectById(Long projectId);

    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId);

    List<Project> findProjectsByMemberId(Long workspaceId, String memberId);

    List<ProjectParticipant> findProjectParticipantsByProjectId(Long projectId);

    void deleteTaskManager(Long projectParticipantId);
}
