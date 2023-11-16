package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WorkspaceParticipantJpaRepository extends JpaRepository<WorkspaceParticipant, Long> {

    @Query("select wsp.workspace " +
           "from WorkspaceParticipant wsp " +
           "where wsp.memberId = :memberId and wsp.workspace.removed = false")
    List<Workspace> findWorkspacesByMemberId(String memberId);

    Optional<WorkspaceParticipant> findByWorkspaceAndMemberId(Workspace workspace, String memberId);

    List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId);
}
