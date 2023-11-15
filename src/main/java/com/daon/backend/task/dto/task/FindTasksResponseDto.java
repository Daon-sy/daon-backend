package com.daon.backend.task.dto.task;

import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskProgressStatus;
import com.daon.backend.task.dto.BoardSummary;
import com.daon.backend.task.dto.ProjectSummary;
import com.daon.backend.task.dto.TaskManager;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class FindTasksResponseDto {

    private int totalCount;

    private List<TaskSummary> tasks;

    public FindTasksResponseDto(List<TaskSummary> tasks) {
        this.totalCount = tasks.size();
        this.tasks = tasks;
    }

    @Getter
    @AllArgsConstructor
    public static class TaskSummary {

        private Long taskId;

        private String title;

        private ProjectSummary project;

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDateTime startDate;

        @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDateTime endDate;

        private TaskProgressStatus progressStatus;

        private boolean emergency;

        private BoardSummary board;

        private boolean bookmark;

        private TaskManager taskManager;

        public TaskSummary(Task task) {
            this.taskId = task.getId();
            this.project = task.getProject() != null ? new ProjectSummary(task.getProject()) : null;
            this.title = task.getTitle();
            this.startDate = task.getStartDate();
            this.endDate = task.getEndDate();
            this.progressStatus = task.getProgressStatus();
            this.emergency = task.isEmergency();
            this.board = task.getBoard() != null ? new BoardSummary(task.getBoard()) : null;
            this.bookmark = task.getTaskBookmarks().stream()
                    .anyMatch(p -> p.getParticipant().getId().equals(task.getTaskManager().getId()));
            this.taskManager = task.getTaskManager() != null ? new TaskManager(task.getTaskManager()) : null;
        }
    }
}
