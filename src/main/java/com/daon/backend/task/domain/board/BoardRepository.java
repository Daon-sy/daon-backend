package com.daon.backend.task.domain.board;

import java.util.Optional;

public interface BoardRepository {

    Optional<Board> findBoardById(Long boardId);

    void deleteTasksRelatedBoard(Long boardId);
}
