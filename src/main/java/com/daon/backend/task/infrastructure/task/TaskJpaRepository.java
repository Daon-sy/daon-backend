package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Task;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = {"taskManager", "board", "taskBookmarks", "board.project", "board.project.workspace"})
    Optional<Task> findTaskById(Long taskId);
}

