package com.daon.backend.task.dto.search;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.domain.task.TaskProgressStatus;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.dto.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Getter
public class SearchResponseDto {

    private PageResponse<WorkspaceResult> workspaces; // 3
    private PageResponse<ProjectResult> projects; // 5
    private PageResponse<TaskResult> tasks; // 10

    public SearchResponseDto(Page<WorkspaceResult> workspaces, Page<ProjectResult> projects, Page<TaskResult> tasks) {
        this.workspaces = new PageResponse<>(workspaces);
        this.projects = new PageResponse<>(projects);
        this.tasks = new PageResponse<>(tasks);
    }

    @Getter
    public static class WorkspaceResult extends WorkspaceSummary {

        private int workspaceParticipantsCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedAt;

        public WorkspaceResult(Workspace workspace, int workspaceParticipantsCount,
                               LocalDateTime createdAt, LocalDateTime modifiedAt) {
            super(workspace);
            this.workspaceParticipantsCount = workspaceParticipantsCount;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }

    @Getter
    public static class ProjectResult extends ProjectSummary {

        private WorkspaceSummary workspace;
        private int projectParticipantsCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedAt;

        public ProjectResult(Long projectId, String title, String description,
                             int projectParticipantsCount, WorkspaceSummary workspaceSummary,
                             LocalDateTime createdAt, LocalDateTime modifiedAt) {
            super(projectId, title, description);
            this.projectParticipantsCount =projectParticipantsCount;
            this.workspace = workspaceSummary;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }

    @Getter
    public static class TaskResult extends TaskSummary {

        private WorkspaceSummary workspace;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedAt;

        public TaskResult(Long taskId, ProjectSummary project, BoardSummary board, TaskManager taskManager,
                          String title, LocalDateTime startDate, LocalDateTime endDate, TaskProgressStatus progressStatus,
                          boolean emergency, boolean bookmark, long replyCount, boolean isTaskManager,
                          WorkspaceSummary workspaceSummary, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            super(taskId, project, board, taskManager, title, startDate, endDate, progressStatus, emergency, bookmark, replyCount, isTaskManager);
            this.workspace = workspaceSummary;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }
}
