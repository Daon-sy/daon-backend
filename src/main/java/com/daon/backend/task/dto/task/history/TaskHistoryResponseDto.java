package com.daon.backend.task.dto.task.history;

import lombok.Getter;
import org.springframework.data.domain.Slice;

@Getter
public class TaskHistoryResponseDto {

    private Slice<TaskHistory> histories;

    public TaskHistoryResponseDto(Slice<TaskHistory> histories) {
        this.histories = histories;
    }
}
