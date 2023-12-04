package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceNoticeJpaRepository extends JpaRepository<WorkspaceNotice, Long> {
}
