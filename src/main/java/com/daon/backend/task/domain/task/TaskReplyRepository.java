package com.daon.backend.task.domain.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskReplyRepository {

    TaskReply save(TaskReply taskReply);

    List<TaskReply> findTaskReplyByTaskId(Long taskId);

    Page<TaskReply> findTaskReplyByTaskId(Long taskId, Pageable pageable);
}
