package com.daon.backend.task.domain.task;

import com.daon.backend.task.dto.ReplySummary;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
    Reply save(Reply reply);

    Optional<Reply> findReplyByReplyId(Long replyId);

    List<ReplySummary> findReplyListByTaskId(Long taskId);
}
