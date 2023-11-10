package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Board;
import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    boolean existsBoardByTitle(String title);

    @EntityGraph(attributePaths = "tasks")
    Optional<Board> findBoardByProjectId(Long projectId);

}
