package com.daon.backend.member.infrastructure;

import com.daon.backend.member.service.WorkspaceInitManager;
import com.daon.backend.task.domain.workspace.WorkspaceCreator;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class WorkspaceInitManagerImpl implements WorkspaceInitManager {

    private final WorkspaceService workspaceService;

    @Override
    public void init(String memberId, String name) {

        WorkspaceCreator creator = WorkspaceCreator.builder()
                .memberId(memberId)
                .profileName(name)
                .build();
        workspaceService.createPersonalWorkspace(creator);
    }
}
