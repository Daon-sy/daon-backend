package com.daon.backend.task.domain.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface WorkspaceNoticeRepository {
    WorkspaceNotice save(WorkspaceNotice workspaceNotice);

    Optional<WorkspaceNotice> findById(Long noticeId);

    void deleteById(Long noticeId);

    Page<WorkspaceNotice> findWorkspaceNoticesByWorkspaceId(Long workspaceId, Pageable pageable);

    Page<WorkspaceNotice> findWorkspaceNoticesByWorkspaceIdAndKeyword(Long workspaceId, String keyword, Pageable pageable);
}
