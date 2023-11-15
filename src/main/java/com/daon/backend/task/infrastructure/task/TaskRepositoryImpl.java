package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;
    private final TaskBookmarkJpaRepository taskBookmarkJpaRepository;

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

    @Override
    public Optional<Task> findTaskByTaskId(Long taskId) {
        return taskJpaRepository.findById(taskId);
    }

    @Override
    public List<Task> findTasksByProjectId(Long projectId) {
        return taskJpaRepository.findByProjectId(projectId);
    }

    @Override
    public boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId) {
        return taskBookmarkJpaRepository.existsTaskBookmarkByTaskIdAndParticipant_Id(taskId, projectParticipantId);
    }

}
