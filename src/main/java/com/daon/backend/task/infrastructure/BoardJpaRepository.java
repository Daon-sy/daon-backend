package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    boolean existsBoardByTitle(String title);

}
