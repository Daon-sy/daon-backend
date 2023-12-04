package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.domain.workspace.WorkspaceNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkspaceNoticeRepositoryImpl implements WorkspaceNoticeRepository {

    private final WorkspaceNoticeJpaRepository workspaceNoticeJpaRepository;

    @Override
    public WorkspaceNotice save(WorkspaceNotice workspaceNotice) {
        return workspaceNoticeJpaRepository.save(workspaceNotice);
    }
}
