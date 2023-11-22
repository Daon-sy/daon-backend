package com.daon.backend.task.infrastructure.board;

import com.daon.backend.task.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {
}
