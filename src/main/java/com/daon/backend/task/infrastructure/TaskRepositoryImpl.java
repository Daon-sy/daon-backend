package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository {

    private final TaskJpaRepository taskJpaRepository;

    @Override
    public Task save(Task task) {
        return taskJpaRepository.save(task);
    }

}
