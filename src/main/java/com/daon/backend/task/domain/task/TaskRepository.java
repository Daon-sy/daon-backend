package com.daon.backend.task.domain.task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findTaskByTaskId(Long taskId);

    List<Task> findTasksByProjectId(Long projectId);

    boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId);
}
