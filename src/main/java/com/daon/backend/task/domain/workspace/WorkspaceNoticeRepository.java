package com.daon.backend.task.domain.workspace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WorkspaceNoticeRepository {
    WorkspaceNotice save(WorkspaceNotice workspaceNotice);
    Optional<WorkspaceNotice> findWorkspaceNoticeById(Long noticeId);
    void deleteById(Long noticeId);
    Page<WorkspaceNotice> findWorkspaceNoticesByWorkspaceId(Long workspaceId, Pageable pageable);
}
