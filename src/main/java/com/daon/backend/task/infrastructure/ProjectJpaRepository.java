package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    Optional<Project> findProjectByIdAndWorkspaceId(Long projectId, Long workspaceId);
}
