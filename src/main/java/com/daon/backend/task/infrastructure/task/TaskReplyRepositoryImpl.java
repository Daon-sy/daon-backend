package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.TaskReply;
import com.daon.backend.task.domain.task.TaskReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskReplyRepositoryImpl implements TaskReplyRepository {

    private final TaskReplyJpaRepository taskReplyJpaRepository;

    @Override
    public TaskReply save(TaskReply taskReply) {
        return taskReplyJpaRepository.save(taskReply);
    }

    @Override
    public List<TaskReply> findTaskReplyByTaskId(Long taskId) {
        return taskReplyJpaRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId);
    }

    @Override
    public Slice<TaskReply> findTaskReplyByTaskId(Long taskId, Pageable pageable) {
        return taskReplyJpaRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId, pageable);
    }
}
