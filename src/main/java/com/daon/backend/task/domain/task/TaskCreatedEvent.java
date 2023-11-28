package com.daon.backend.task.domain.task;

import lombok.Getter;

@Getter
public class TaskCreatedEvent {

    private Task task;

    public TaskCreatedEvent(Task task) {
        this.task = task;
    }
}
