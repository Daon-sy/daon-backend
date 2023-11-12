package com.daon.backend.task.domain.workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Optional<Workspace> findWorkspaceByWorkspaceId(Long workspaceId);

    List<Workspace> findWorkspacesByMemberId(String memberId);

    Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceAndMemberId(Workspace workspace, String memberId);

    Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceParticipantId(Long workspaceParticipantId);

    Optional<Workspace> findWorkspaceByJoinCode(String joinCode);

    List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId);

    boolean existsWorkspaceParticipantByMemberIdAndWorkspaceId(String memberId, Long workspaceId);

    Role findParticipantRoleByMemberIdAndWorkspaceId(String memberId, Long workspaceId);
}
