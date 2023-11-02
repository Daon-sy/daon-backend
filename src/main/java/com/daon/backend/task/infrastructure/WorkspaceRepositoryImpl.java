package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkspaceRepositoryImpl implements WorkspaceRepository {

    private final WorkspaceJpaRepository workspaceJpaRepository;


    @Override
    public Workspace save(Workspace workspace) {
        return workspaceJpaRepository.save(workspace);
    }

    @Override
    public List<Workspace> findAllWorkspace() {
        return workspaceJpaRepository.findAll();
    }
}
