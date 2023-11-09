package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.Task;
import com.daon.backend.task.domain.project.TaskBookmark;
import com.daon.backend.task.domain.project.TaskProgressStatus;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class TaskListResponseDto {


    private List<TaskSummary> tasks;

    public TaskListResponseDto(List<TaskSummary> tasks) {
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

        public TaskManager(WorkspaceParticipant workspaceParticipant) {
            this.participantId = workspaceParticipant.getId();
            this.name = workspaceParticipant.getProfile().getName();
            this.profileImageUrl = workspaceParticipant.getProfile().getImageUrl();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TaskSummary {

        private Long taskId;
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
            this.title = task.getTitle();
            this.startDate = task.getStartDate();
            this.endDate = task.getEndDate();
            this.progressStatus = task.getProgressStatus();
            this.emergency = task.isEmergency();
            this.board = new BoardSummary(task.getBoard().getId(), task.getBoard().getTitle());
            this.bookmark = task.getTaskBookmarks().stream()
                    .anyMatch(p -> p.getId().equals(task.getWorkspaceParticipant().getId()));
            this.taskManager = new TaskManager(task.getWorkspaceParticipant());
        }
    }
}
