package com.daon.backend.task.domain.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskReplyRepository {

    TaskReply save(TaskReply taskReply);

    Page<TaskReply> findTaskReplyByTaskId(Long taskId, Pageable pageable);
}
