package com.daon.backend.task.dto;

import com.daon.backend.task.domain.task.TaskProgressStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TaskDetail {

    private Long taskId;
    private ProjectSummary project;
    private BoardSummary board;
    private TaskManager taskManager;
    private String title;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    private TaskProgressStatus progressStatus;
    private boolean emergency;
    private boolean bookmark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public TaskDetail(Long taskId, ProjectSummary project, BoardSummary board,
                      TaskManager taskManager, String title, String content,
                      LocalDateTime startDate, LocalDateTime endDate,
                      TaskProgressStatus progressStatus, boolean emergency,
                      boolean bookmark, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.taskId = taskId;
        this.project = project;
        this.board = board;
        this.taskManager = taskManager;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.progressStatus = progressStatus;
        this.emergency = emergency;
        this.bookmark = bookmark;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
