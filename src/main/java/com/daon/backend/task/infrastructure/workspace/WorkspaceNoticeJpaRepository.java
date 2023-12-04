package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkspaceNoticeJpaRepository extends JpaRepository<WorkspaceNotice, Long> {

    @EntityGraph(attributePaths = {"workspace"})
    Optional<WorkspaceNotice> findWorkspaceNoticeById(Long noticeId);

    List<WorkspaceNotice> findWorkspaceNoticeByWorkspaceIdOrderByCreatedAtAsc(Long workspaceId);
}
