package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Reply;
import com.daon.backend.task.dto.ReplySummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findByIdAndRemovedFalse(Long replyId);

    List<ReplySummary> findAllByTaskId(Long taskId);

}
