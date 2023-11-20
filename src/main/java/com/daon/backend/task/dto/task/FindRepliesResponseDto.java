package com.daon.backend.task.dto.task;

import com.daon.backend.task.dto.ReplySummary;
import lombok.Getter;

import java.util.List;

@Getter
public class FindRepliesResponseDto {

    private int totalCount;

    private Long taskId;

    private List<ReplySummary> replies;

    public FindRepliesResponseDto(List<ReplySummary> replies, Long taskId) {
        this.totalCount = replies.size();
        this.replies = replies;
        this.taskId = taskId;
    }
}
