package com.daon.backend.member.infrastructure;

import com.daon.backend.member.service.MemberServiceThroughTask;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceThroughTaskImpl implements MemberServiceThroughTask {

    private final WorkspaceRepository workspaceRepository;

    @Override
    public void deleteRelatedTaskDomains(String memberId) {
        List<Workspace> workspaces = workspaceRepository.findWorkspacesByMemberId(memberId);
        workspaces.forEach(workspace -> {
                    Long workspaceId = workspace.getId();

                    if (workspace.canWithdrawWorkspace(memberId)) {
                        Long workspaceParticipantId = workspace.findWorkspaceParticipantByMemberId(memberId).getId();
                        workspaceRepository.deleteTaskManager(workspaceParticipantId);
                        workspace.withdrawWorkspace(memberId);
                    } else {
                        workspaceRepository.deleteById(workspaceId);
                    }
                });
//        workspaces.forEach(workspace -> workspaceService.withdrawWorkspace(workspace.getId()));
    }
}
