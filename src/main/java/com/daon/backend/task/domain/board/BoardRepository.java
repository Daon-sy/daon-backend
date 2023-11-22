package com.daon.backend.task.domain.board;

import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findBoardByBoardId(Long boardId);

    void deleteTasksRelatedBoard(Long boardId);
}
