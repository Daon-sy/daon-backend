package com.daon.backend.task.infrastructure.board;

import com.daon.backend.task.domain.board.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"project", "tasks"})
    Optional<Board> findBoardById(Long boardId);

    List<Board> findBoardsByProjectIdOrderByCreatedAtAsc(Long projectId);
}
