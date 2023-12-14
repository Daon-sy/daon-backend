package com.daon.backend.notification.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDataDto {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime time;

    private WorkspaceData workspace;
    private ProjectData project;
    private BoardData board;
    private TaskData task;

    @Builder
    public NotificationDataDto(LocalDateTime time, WorkspaceData workspace, ProjectData project, BoardData board, TaskData task) {
        this.time = time;
        this.workspace = workspace;
        this.project = project;
        this.board = board;
        this.task = task;
    }

    @Getter
    public static class WorkspaceData {
        private Long workspaceId;
        private String workspaceTitle;

        public WorkspaceData(Long workspaceId, String workspaceTitle) {
            this.workspaceId = workspaceId;
            this.workspaceTitle = workspaceTitle;
        }
    }

    @Getter
    public static class ProjectData {
        private Long projectId;
        private String projectTitle;

        public ProjectData(Long projectId, String projectTitle) {
            this.projectId = projectId;
            this.projectTitle = projectTitle;
        }
    }

    @Getter
    public static class BoardData {
        private Long boardId;
        private String boardTitle;

        public BoardData(Long boardId, String boardTitle) {
            this.boardId = boardId;
            this.boardTitle = boardTitle;
        }
    }

    @Getter

    public static class TaskData {
        private Long taskId;
        private String taskTitle;

        public TaskData(Long taskId, String taskTitle) {
            this.taskId = taskId;
            this.taskTitle = taskTitle;
        }
    }
}
