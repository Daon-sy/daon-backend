package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.TaskReply;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskReplyJpaRepository extends JpaRepository<TaskReply, Long> {

    List<TaskReply> findAllByTaskIdOrderByCreatedAtDesc(Long taskId);

    Slice<TaskReply> findAllByTaskIdOrderByCreatedAtDesc(Long taskId, Pageable pageable);

}
