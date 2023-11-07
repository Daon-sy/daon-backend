package com.daon.backend.task.domain.project;

import java.util.List;

public interface BoardRepository {

    Board save(Board board);

    boolean existsBoardByTitle(String title);

    List<Board> findBoardsByWorkspaceIdAndProjectId(Long workspaceId, Long projectId);
}
