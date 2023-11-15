package com.daon.backend.task.dto.response;

import com.daon.backend.task.domain.project.Task;
import com.daon.backend.task.domain.project.TaskProgressStatus;
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
