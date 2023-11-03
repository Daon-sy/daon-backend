package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.workspace.WorkspaceParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectParticipantJpaRepository extends JpaRepository<ProjectParticipant, Long> {

    @Query("select pjp.project " +
           "from ProjectParticipant pjp " +
           "where pjp.workspaceParticipant = :workspaceParticipant")
    List<Project> findProjectsByWorkspaceParticipant(WorkspaceParticipant workspaceParticipant);
}