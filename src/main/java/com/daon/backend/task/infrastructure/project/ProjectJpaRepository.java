package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    // boards는 batch_size로 해결
    @EntityGraph(attributePaths = {"workspace", "participants"})
    Optional<Project> findByIdAndRemovedFalse(Long projectId);

    @EntityGraph(attributePaths = "participants")
    Optional<Project> findProjectWithParticipantsByIdAndRemovedFalse(Long projectId);

    @EntityGraph(attributePaths = "boards")
    Optional<Project> findProjectWithBoardsByIdAndRemovedFalse(Long projectId);

    List<Project> findAllProjectsByWorkspaceId(Long workspaceId);
}
