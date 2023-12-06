package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.TaskReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskReplyJpaRepository extends JpaRepository<TaskReply, Long> {

    Page<TaskReply> findAllByTaskIdOrderByCreatedAtDesc(Long taskId, Pageable pageable);

}
