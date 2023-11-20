package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Reply;
import com.daon.backend.task.domain.task.ReplyRepository;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.dto.ReplySummary;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReplyRepositoryImpl implements ReplyRepository {

    private final JPAQueryFactory queryFactory;
    private final ReplyJpaRepository replyJpaRepository;

    @Override
    public Reply save(Reply reply) {
        return replyJpaRepository.save(reply);
    }

    @Override
    public Optional<Reply> findReplyByReplyId(Long replyId) {
        return replyJpaRepository.findByIdAndRemovedFalse(replyId);
    }

    @Override
    public List<ReplySummary> findReplyListByTaskId(Long taskId) {
        return replyJpaRepository.findAllByTaskId(taskId);
    }
}
