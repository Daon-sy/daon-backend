package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    boolean existsBoardByTitle(String title);

    List<Board> findBoardsByWorkspaceIdAndProjectId(Long workspaceId, Long projectId);

}
