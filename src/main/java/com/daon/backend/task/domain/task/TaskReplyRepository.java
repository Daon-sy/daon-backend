package com.daon.backend.task.domain.task;

import java.util.List;

public interface TaskReplyRepository {

    TaskReply save(TaskReply taskReply);

    List<TaskReply> findTaskReplyByTaskId(Long taskId);
}
