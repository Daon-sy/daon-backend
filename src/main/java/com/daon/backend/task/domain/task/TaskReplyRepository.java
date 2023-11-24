package com.daon.backend.task.domain.task;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface TaskReplyRepository {

    TaskReply save(TaskReply taskReply);

    List<TaskReply> findTaskReplyByTaskId(Long taskId);

    Slice<TaskReply> findTaskReplyByTaskId(Long taskId, Pageable pageable);
}
