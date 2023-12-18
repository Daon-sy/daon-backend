package com.daon.backend.member.infrastructure;

import com.daon.backend.member.service.MemberServiceThroughTask;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.domain.workspace.exception.CanNotDeletePersonalWorkspaceException;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberServiceThroughTaskImpl implements MemberServiceThroughTask {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceService workspaceService;

    @Override
    public void deleteRelatedTaskDomains(String memberId) {
        List<Long> workspaceIds = workspaceRepository.findWorkspacesByMemberId(memberId).stream()
                .map(Workspace::getId)
                .collect(Collectors.toList());
        for (Long workspaceId : workspaceIds) {
            try {
                workspaceService.withdrawWorkspace(workspaceId);
            } catch (CanNotDeletePersonalWorkspaceException e) {
                workspaceRepository.deleteById(workspaceId);
            }
        }
    }
}
