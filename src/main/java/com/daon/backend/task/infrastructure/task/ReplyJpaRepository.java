package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {

    List<Reply> findAllByTaskId(Long taskId);

}
