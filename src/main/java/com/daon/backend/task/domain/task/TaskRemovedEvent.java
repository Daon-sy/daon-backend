package com.daon.backend.task.domain.task;

import lombok.Getter;

@Getter
public class TaskRemovedEvent {

    private Task task;

    public TaskRemovedEvent(Task task) {
        this.task = task;
    }
}
