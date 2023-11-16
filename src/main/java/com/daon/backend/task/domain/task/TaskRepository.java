package com.daon.backend.task.domain.task;

import com.daon.backend.task.domain.project.Board;
import com.daon.backend.task.dto.TaskDetail;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.TaskSummary;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findTaskByTaskId(Long taskId);

    boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId);

    List<TaskSummary> findTaskSummaries(String memberId, TaskSearchParams params);

    Optional<TaskDetail> findTaskDetail(String memberId, Long taskId);

    List<Task> findTasksByBoard(Board board);
}
