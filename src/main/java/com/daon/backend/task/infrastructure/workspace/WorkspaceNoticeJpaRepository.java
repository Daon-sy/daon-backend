package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceNoticeJpaRepository extends JpaRepository<WorkspaceNotice, Long> {
    Optional<WorkspaceNotice> findWorkspaceNoticeById(Long noticeId);
    void deleteById(Long noticeId);
    Page<WorkspaceNotice> findWorkspaceNoticeByWorkspaceIdOrderByCreatedAtDesc(Long workspaceId, Pageable pageable);
    Page<WorkspaceNotice> findWorkspaceNoticeByWorkspaceIdAndContentContainingOrderByCreatedAtDesc(Long workspaceId, String keyword, Pageable pageable);
}
