package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.Task;
import com.daon.backend.task.domain.project.TaskProgressStatus;
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

        private LocalDateTime startDate;

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
