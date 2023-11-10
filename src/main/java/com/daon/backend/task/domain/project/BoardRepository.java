package com.daon.backend.task.domain.project;

import java.util.Optional;

public interface BoardRepository {

    boolean existsBoardByTitle(String title);

    Optional<Board> findBoardByProjectId(Long projectId);

}
