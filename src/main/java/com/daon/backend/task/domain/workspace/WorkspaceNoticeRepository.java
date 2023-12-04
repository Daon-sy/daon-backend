package com.daon.backend.task.domain.workspace;

import java.util.List;
import java.util.Optional;

public interface WorkspaceNoticeRepository {
    WorkspaceNotice save(WorkspaceNotice workspaceNotice);

    List<WorkspaceNotice> findWorkspacesByWorkspaceId(Long workspaceId);

    Optional<WorkspaceNotice> findWorkspaceNoticeById(Long noticeId);

}
