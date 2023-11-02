package com.daon.backend.task.domain.workspace;

import java.util.List;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    List<Workspace> findAllWorkspace();

}
