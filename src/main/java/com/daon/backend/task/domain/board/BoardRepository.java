package com.daon.backend.task.domain.board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findBoardById(Long boardId);

    List<Board> findBoardsByProjectIdOrderByDESC(Long projectId);

    void deleteTasksRelatedBoard(Long boardId);
}
