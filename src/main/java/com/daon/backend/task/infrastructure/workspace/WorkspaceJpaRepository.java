package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.Workspace;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {

    @EntityGraph(attributePaths = "participants")
    Optional<Workspace> findWorkspaceWithParticipantsById(Long workspaceId);

}
