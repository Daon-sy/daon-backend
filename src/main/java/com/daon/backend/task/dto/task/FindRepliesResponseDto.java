package com.daon.backend.task.dto.task;

import com.daon.backend.task.dto.ReplySummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindRepliesResponseDto {

    private int totalCount;

    private Long taskId;

    private List<ReplySummary> Replies;

    @Builder
    public FindRepliesResponseDto(List<ReplySummary> Replies, Long taskId) {
        this.totalCount = Replies.size();
        this.Replies = Replies;
        this.taskId = taskId;
    }
}
