package com.daon.backend.task.domain.task;

import com.daon.backend.task.dto.TaskDetail;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.TaskSearchResult;
import com.daon.backend.task.dto.TaskSummary;
import com.daon.backend.task.dto.task.history.TaskHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findTaskById(Long taskId);

    boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId);

    List<Task> findTasksForLessThanThreeDaysOld();

    List<TaskSummary> findTaskSummaries(String memberId, Long workspaceId, TaskSearchParams params);

    Slice<TaskSearchResult> searchTaskSummariesByTitle(String memberId, String title, Pageable pageable);

    Optional<TaskDetail> findTaskDetail(String memberId, Long taskId);

    Slice<TaskHistory> findTaskHistoriesByProjectIdAndTaskId(Long projectId, Long taskId, Pageable pageable);

    void deleteAllTaskBookmark(Long taskId);
}
