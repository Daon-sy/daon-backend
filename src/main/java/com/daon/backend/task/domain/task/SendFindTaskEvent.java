package com.daon.backend.task.domain.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendFindTaskEvent {

    Long taskId;
}
