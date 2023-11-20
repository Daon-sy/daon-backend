package com.daon.backend.task.domain.task;

import java.util.List;

public interface ReplyRepository {

    Reply save(Reply reply);

    List<Reply> findReplyListByTaskId(Long taskId);
}
