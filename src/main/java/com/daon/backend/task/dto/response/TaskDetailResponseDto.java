package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskDetailResponseDto {

    private Long taskId;

    private ProjectDto project;

    private BoardDto board;

    private String title;
    private String content;
    private TaskManagerDto taskManager;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    private TaskProgressStatus progressStatus;
    private boolean emergency;
    private boolean bookmark;

    @Getter
    public static class ProjectDto {
        private Long projectId;
        private String title;
        private String description;

        public ProjectDto(Project project) {
            this.projectId = project.getId();
            this.title = project.getTitle();
            this.description = project.getDescription();
        }
    }

    @Getter
    public static class BoardDto {
        private Long boardId;
        private String title;

        public BoardDto(Board board) {
            this.boardId = board.getId();
            this.title = board.getTitle();
        }
    }

    @Getter
    public static class TaskManagerDto {
        private Long participantId;
        private String name;
        private String imageUrl;

        public TaskManagerDto(ProjectParticipant participant) {
            this.participantId = participant.getId();
            this.name = participant.getWorkspaceParticipant().getProfile().getName();
            this.imageUrl = participant.getWorkspaceParticipant().getProfile().getImageUrl();
        }
    }

    public TaskDetailResponseDto(Task task) {
        this.taskId = task.getId();
        this.project = new ProjectDto(task.getProject());
        this.board = new BoardDto(task.getBoard());
        this.title = task.getTitle();
        this.content = task.getContent();
        this.taskManager = new TaskManagerDto(task.getTaskManager());
        this.startDate = task.getStartDate();
        this.endDate = task.getEndDate();
        this.progressStatus = task.getProgressStatus();
        this.emergency = task.isEmergency();
        this.bookmark = task.getTaskBookmarks().stream()
                .anyMatch(p -> p.getId().equals(task.getTaskManager().getId()));
    }
}
