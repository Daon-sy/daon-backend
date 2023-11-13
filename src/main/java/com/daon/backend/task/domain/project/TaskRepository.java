package com.daon.backend.task.domain.project;

import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findTaskByTaskId(Long taskId);

    boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId);
}
