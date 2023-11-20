package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Reply;
import com.daon.backend.task.domain.task.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

    private final ReplyJpaRepository replyJpaRepository;

    @Override
    public Reply save(Reply reply) {
        return replyJpaRepository.save(reply);
    }

    @Override
    public List<Reply> findReplyListByTaskId(Long taskId) {
        return replyJpaRepository.findAllByTaskId(taskId);
    }
}
