package com.daon.backend.task.domain.task;

import com.daon.backend.task.dto.TaskDetail;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.TaskSearchResult;
import com.daon.backend.task.dto.TaskSummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findTaskById(Long taskId);

    List<Task> findAllTasksByProjectId(Long projectId);

    boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId);

    List<TaskSummary> findTaskSummaries(String memberId, TaskSearchParams params);

    Slice<TaskSearchResult> searchTaskSummariesByTitle(String memberId, String title, Pageable pageable);

    Optional<TaskDetail> findTaskDetail(String memberId, Long taskId);
}
