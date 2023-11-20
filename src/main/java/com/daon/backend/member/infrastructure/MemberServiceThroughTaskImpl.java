package com.daon.backend.member.infrastructure;

import com.daon.backend.member.service.MemberServiceThroughTask;
import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceThroughTaskImpl implements MemberServiceThroughTask {

    private final WorkspaceService workspaceService;
    private final WorkspaceRepository workspaceRepository;

    @Override
    public void deleteRelatedTaskDomains(String memberId) {
        List<Workspace> workspaces = workspaceRepository.findWorkspacesByMemberId(memberId);
        workspaces.forEach(workspace -> {
            List<WorkspaceParticipant> workspaceParticipants = workspaceRepository.findWorkspaceParticipantsByWorkspaceId(workspace.getId());
            long adminCount = workspaceParticipants.stream()
                    .filter(workspaceParticipant -> Role.WORKSPACE_ADMIN.equals(workspaceParticipant.getRole()))
                    .count();
            if (adminCount != 1) {
                workspaceService.withdrawWorkspace(workspace.getId());
            } else {
                workspaceService.deleteWorkspace(workspace.getId());
            }
        });
    }
}
