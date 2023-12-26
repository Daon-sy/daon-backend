package com.daon.backend.member.infrastructure;

import com.daon.backend.member.service.MemberServiceThroughTask;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceThroughTaskImpl implements MemberServiceThroughTask {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceService workspaceService;

    @Override
    public void deleteRelatedTaskDomains(String memberId) {
        List<Workspace> workspaces = workspaceRepository.findWorkspacesByMemberId(memberId);
        for (Workspace workspace : workspaces) {
            if (workspace.isPersonal()) {
                workspaceRepository.deleteById(workspace.getId());
                continue;
            }
            if (!workspace.isPersonal()) workspaceService.withdrawWorkspace(workspace.getId());
        }
    }
}
