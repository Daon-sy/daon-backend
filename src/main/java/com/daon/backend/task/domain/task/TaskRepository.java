package com.daon.backend.task.domain.task;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.task.dto.TaskDetail;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.TaskSearchResult;
import com.daon.backend.task.dto.TaskSummary;
import com.daon.backend.task.dto.task.history.TaskHistory;
import com.daon.backend.task.dto.task.history.TaskHistoryResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findTaskByTaskId(Long taskId);

    List<Task> findTasksByProjectId(Long projectId);

    List<Task> findAllTasksByProjectId(Long projectId);

    boolean existsTaskBookmarkByTaskIdAndProjectParticipantId(Long taskId, Long projectParticipantId);

    List<TaskSummary> findTaskSummaries(String memberId, TaskSearchParams params);

    Slice<TaskSearchResult> searchTaskSummariesByTitle(String memberId, String title, Pageable pageable);

    Optional<TaskDetail> findTaskDetail(String memberId, Long taskId);

    List<Task> findTasksByProjectIdAndBoardId(Long projectId, Long boardId);

    Slice<TaskHistory> findTaskHistoriesByProjectIdAndTaskId(Long projectId, Long taskId, Pageable pageable);
}
