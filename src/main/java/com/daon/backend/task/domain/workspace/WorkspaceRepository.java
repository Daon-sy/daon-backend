package com.daon.backend.task.domain.workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Optional<Workspace> findWorkspaceById(Long workspaceId);

    List<Workspace> findWorkspacesByMemberId(String memberId);

    Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceAndMemberId(Workspace workspace, String memberId);

}
