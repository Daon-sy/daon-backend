package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceParticipantJpaRepository extends JpaRepository<WorkspaceParticipant, Long> {

    List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceIdOrderByCreatedAtAsc(Long workspaceId);
}
