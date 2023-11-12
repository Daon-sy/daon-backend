package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Task;
import com.daon.backend.task.domain.project.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    @Override
    public Optional<Task> findTaskByTaskId(Long taskId) {
        return taskJpaRepository.findById(taskId);
    }

}
