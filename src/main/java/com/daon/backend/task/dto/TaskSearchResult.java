package com.daon.backend.task.dto;

import com.daon.backend.task.domain.task.TaskProgressStatus;

import java.time.LocalDateTime;

public class TaskSearchResult extends TaskDetail {

    public TaskSearchResult(Long taskId, ProjectSummary project, TaskManager taskManager,
                            String title, LocalDateTime startDate, LocalDateTime endDate,
                            TaskProgressStatus progressStatus, boolean emergency) {
        super(
                taskId,
                project,
                taskManager,
                title,
                startDate,
                endDate,
                progressStatus,
                emergency
        );
    }
}
