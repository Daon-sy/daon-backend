package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.Task;
import com.daon.backend.task.domain.project.TaskProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class TaskListResponseDto {

    private int totalCount;
    private List<TaskSummary> tasks;

    public TaskListResponseDto(List<TaskSummary> tasks) {
        this.totalCount = tasks.size();
        this.tasks = tasks;
    }

    @Getter
    @AllArgsConstructor
    public static class BoardSummary {
        private Long boardId;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    public static class TaskManager {
        private Long participantId;
        private String name;
        private String profileImageUrl;

        public TaskManager(ProjectParticipant participant) {
            this.participantId = participant.getId();
            this.name = participant.getWorkspaceParticipant().getProfile().getName();
            this.profileImageUrl = participant.getWorkspaceParticipant().getProfile().getImageUrl();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TaskSummary {

        private Long taskId;
        private Long projectId;
        private String title;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private TaskProgressStatus progressStatus;
        private boolean emergency;
        private BoardSummary board;
        private boolean bookmark;
        private TaskManager taskManager;

        public TaskSummary(Task task) {
            this.taskId = task.getId();
            this.projectId = task.getProject().getId();
            this.title = task.getTitle();
            this.startDate = task.getStartDate();
            this.endDate = task.getEndDate();
            this.progressStatus = task.getProgressStatus();
            this.emergency = task.isEmergency();
            this.board = task.getBoard() != null ? new BoardSummary(task.getBoard().getId(), task.getBoard().getTitle()) : null;
            this.bookmark = task.getTaskBookmarks().stream()
                    .anyMatch(p -> p.getId().equals(task.getTaskManager().getId()));
            this.taskManager = task.getTaskManager() != null ? new TaskManager(task.getTaskManager()) : null;
        }
    }
}
