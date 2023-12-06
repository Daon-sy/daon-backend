package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.domain.workspace.WorkspaceNoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkspaceNoticeRepositoryImpl implements WorkspaceNoticeRepository {

    private final WorkspaceNoticeJpaRepository workspaceNoticeJpaRepository;

    @Override
    public WorkspaceNotice save(WorkspaceNotice workspaceNotice) {
        return workspaceNoticeJpaRepository.save(workspaceNotice);
    }

    @Override
    public List<WorkspaceNotice> findWorkspaceNoticesByWorkspaceId(Long workspaceId) {
        return workspaceNoticeJpaRepository.findWorkspaceNoticeByWorkspaceIdOrderByCreatedAtDesc(workspaceId);
    }

    @Override
    public Optional<WorkspaceNotice> findWorkspaceNoticeById(Long noticeId) {
        return workspaceNoticeJpaRepository.findWorkspaceNoticeById(noticeId);
    }

    @Override
    public void deleteById(Long noticeId) {
        workspaceNoticeJpaRepository.deleteById(noticeId);
    }


}
