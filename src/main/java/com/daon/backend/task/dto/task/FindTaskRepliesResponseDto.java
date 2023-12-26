package com.daon.backend.task.dto.task;

import com.daon.backend.task.dto.TaskReplySummary;
import lombok.Getter;

import java.util.List;

@Getter
public class FindTaskRepliesResponseDto {

    private int totalCount;

    private Long taskId;

    private List<TaskReplySummary> taskReplies;

    public FindTaskRepliesResponseDto(List<TaskReplySummary> taskReplies, Long taskId) {
        this.totalCount = taskReplies.size();
        this.taskReplies = taskReplies;
        this.taskId = taskId;
    }
}
