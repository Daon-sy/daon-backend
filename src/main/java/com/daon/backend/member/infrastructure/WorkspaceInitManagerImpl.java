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
        String defaultTitleSuffix = "님의 개인 워크스페이스 공간";

        WorkspaceCreator creator = WorkspaceCreator.builder()
                .memberId(memberId)
                .profileName(name + defaultTitleSuffix)
                .build();
        workspaceService.createPersonalWorkspace(creator);
    }
}
