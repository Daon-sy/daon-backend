package com.daon.backend.task.dto.task;

import com.daon.backend.task.dto.TaskSummary;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class FindTasksResponseDto {

    private int totalCount;
    private List<TaskSummary> tasks;

    public FindTasksResponseDto(List<TaskSummary> tasks) {
        this.totalCount = tasks.size();
        this.tasks = tasks;
    }
}
