package com.daon.backend.task.dto;

import com.daon.backend.task.domain.task.TaskProgressStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class  TaskSummary {

    private Long taskId;
    private ProjectSummary project;
    private BoardSummary board;
    private TaskManager taskManager;
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    private TaskProgressStatus progressStatus;
    private boolean emergency;
    private boolean bookmark;
    private long replyCount;

    public TaskSummary(Long taskId, ProjectSummary project, BoardSummary board,
                       TaskManager taskManager, String title, LocalDateTime startDate,
                       LocalDateTime endDate, TaskProgressStatus progressStatus,
                       boolean emergency, boolean bookmark, long replyCount) {
        this.taskId = taskId;
        this.project = project;
        this.board = board;
        this.taskManager = taskManager;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.progressStatus = progressStatus;
        this.emergency = emergency;
        this.bookmark = bookmark;
        this.replyCount = replyCount;
    }
}
