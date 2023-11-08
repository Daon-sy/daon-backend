package com.daon.backend.task.domain.project;

import com.daon.backend.task.domain.workspace.WorkspaceParticipant;

import java.util.List;

public interface TaskRepository {

    Task save(Task task);

    List<Task> findTaskByProjectParticipant(ProjectParticipant projectParticipant);

}
