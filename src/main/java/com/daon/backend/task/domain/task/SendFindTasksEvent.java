package com.daon.backend.task.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendFindTasksEvent {

    private Long workspaceId;

    private Long projectId;

    private Long boardId;
}
