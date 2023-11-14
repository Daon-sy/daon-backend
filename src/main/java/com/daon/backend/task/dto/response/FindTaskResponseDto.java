package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FindTaskResponseDto {

    private Long taskId;

    private ProjectSummary project;

    private BoardSummary board;

    private String title;

    private String content;

    private TaskManager taskManager;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    private TaskProgressStatus progressStatus;

    private boolean emergency;

    private boolean bookmark;

    @Getter
    public static class ProjectSummary {

        private Long projectId;

        private String title;

        private String description;

        public ProjectSummary(Project project) {
            this.projectId = project.getId();
            this.title = project.getTitle();
            this.description = project.getDescription();
        }
    }

    @Getter
    public static class BoardSummary {

        private Long boardId;

        private String title;

        public BoardSummary(Board board) {
            this.boardId = board.getId();
            this.title = board.getTitle();
        }
    }

    @Getter
    public static class TaskManager {

        private Long projectParticipantId;

        private String name;

        private String imageUrl;

        public TaskManager(ProjectParticipant participant) {
            this.projectParticipantId = participant.getId();
            this.name = participant.getWorkspaceParticipant().getProfile().getName();
            this.imageUrl = participant.getWorkspaceParticipant().getProfile().getImageUrl();
        }
    }

    public FindTaskResponseDto(Task task) {
        this.taskId = task.getId();
        this.project = new ProjectSummary(task.getProject());
        this.board = new BoardSummary(task.getBoard());
        this.title = task.getTitle();
        this.content = task.getContent();
        this.taskManager = new TaskManager(task.getTaskManager());
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
        this.progressStatus = task.getProgressStatus();
        this.emergency = task.isEmergency();
        this.bookmark = task.getTaskBookmarks().stream()
                .anyMatch(p -> p.getParticipant().getId().equals(task.getTaskManager().getId()));
    }
}
