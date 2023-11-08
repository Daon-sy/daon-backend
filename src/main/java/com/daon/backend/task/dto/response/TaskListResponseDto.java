package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.Profile;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
public class TaskListResponseDto {

    private Long workspaceId;
    private Long projectId;
    private List<TaskSummary> tasks;

    public TaskListResponseDto(Long workspaceId, Long projectId, List<TaskSummary> tasks) {
        this.workspaceId = workspaceId;
        this.projectId = projectId;
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

        public TaskManager(Long participantId, WorkspaceParticipant workspaceParticipant) {
            this.participantId = participantId;
            this.name = workspaceParticipant.getProfile().getName();
            this.profileImageUrl = workspaceParticipant.getProfile().getImageUrl();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class TaskSummary {

        private Long taskId;
        private String title;
        private LocalDate startDate;
        private LocalDate endDate;
        private TaskProgressStatus progressStatus;
        private BoardSummary board;
        private boolean emergency;
        private TaskBookmark taskBookmark;

        public TaskSummary(Task task, BoardSummary board, TaskBookmark taskBookmark) {
            this.taskId = task.getId();
            this.title = task.getTitle();
            this.progressStatus = task.getProgressStatus();
            this.board = board;
            this.emergency = task.isEmergency();
            this.taskBookmark = taskBookmark;

        }


    }
}
