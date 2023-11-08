package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = "boards")
    Optional<Project> findProjectByIdAndWorkspaceId(Long projectId, Long workspaceId);
}

