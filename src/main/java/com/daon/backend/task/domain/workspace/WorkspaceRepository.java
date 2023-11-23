package com.daon.backend.task.domain.workspace;

import com.daon.backend.task.dto.WorkspaceSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Optional<Workspace> findWorkspaceById(Long workspaceId);

    Optional<Workspace> findWorkspaceWithParticipantsByWorkspaceId(Long workspaceId);

    List<Workspace> findWorkspacesByMemberId(String memberId);

    Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceIdAndMemberId(Long workspaceId, String memberId);

    Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceParticipantId(Long workspaceParticipantId);

    List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId);

    Role findParticipantRoleByMemberIdAndWorkspaceId(String memberId, Long workspaceId);

    Slice<WorkspaceSummary> searchWorkspaceSummariesByTitle(String memberId, String title, Pageable pageable);

    void deleteAllRelatedWorkspace(Long workspaceId);

    void deleteAllRelatedWorkspaceParticipant(Long workspaceParticipantId, String memberId);
}
