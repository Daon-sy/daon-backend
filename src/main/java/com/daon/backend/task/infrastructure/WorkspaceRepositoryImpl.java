package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJpaRepository workspaceJpaRepository;
    private final WorkspaceParticipantJpaRepository workspaceParticipantJpaRepository;

    @Override
    public Workspace save(Workspace workspace) {
        return workspaceJpaRepository.save(workspace);
    }

    @Override
    public Optional<Workspace> findWorkspaceById(Long workspaceId) {
        return workspaceJpaRepository.findById(workspaceId);
    }

    @Override
    public List<Workspace> findWorkspacesByMemberId(String memberId) {
        // 회원 아이디로
        return workspaceParticipantJpaRepository.findWorkspacesByMemberId(memberId);
    }

    @Override
    public Optional<WorkspaceParticipant> findWorkspaceParticipantByWorkspaceAndMemberId(Workspace workspace, String memberId) {
        return workspaceParticipantJpaRepository.findByWorkspaceAndMemberId(workspace, memberId);
    }

    @Override
    public Optional<Workspace> findWorkspaceByJoinCode(String joinCode) {
        return workspaceJpaRepository.findWorkspaceByJoinCode(joinCode);
    }
}
