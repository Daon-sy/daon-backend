package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {
}
