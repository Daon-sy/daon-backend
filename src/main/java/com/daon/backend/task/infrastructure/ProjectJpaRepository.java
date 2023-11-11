package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "boards")
    Optional<Project> findProjectByIdAndWorkspaceId(Long projectId, Long workspaceId);

    // participants 함께 조회
    // boards는 batch_size로 해결
    @Override
    @EntityGraph(attributePaths = {"workspace", "participants"})
    Optional<Project> findById(Long projectId);
}
