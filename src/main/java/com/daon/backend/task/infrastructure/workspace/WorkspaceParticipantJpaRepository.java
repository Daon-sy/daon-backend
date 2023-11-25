package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceParticipantJpaRepository extends JpaRepository<WorkspaceParticipant, Long> {

    Optional<WorkspaceParticipant> findByWorkspaceIdAndMemberId(Long workspaceId, String memberId);

    List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceIdOrderByCreatedAtDesc(Long workspaceId);
}
