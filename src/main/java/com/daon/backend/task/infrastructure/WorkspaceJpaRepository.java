package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.workspace.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {

    Optional<Workspace> findWorkspaceByJoinCode(String joinCode);
}
