package com.daon.backend.task.dto.task.history;

import lombok.Getter;

import java.util.List;

@Getter
public class TaskHistoryResponseDto {

    private List<TaskHistory> histories;

    public TaskHistoryResponseDto(List<TaskHistory> histories) {
        this.histories = histories;
    }
}
