package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.Workspace;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {

    @EntityGraph(attributePaths = "participants")
    Optional<Workspace> findWorkspaceById(Long workspaceId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Task t " +
            "SET t.taskManager = null " +
            "WHERE t.taskManager IN (SELECT pp FROM ProjectParticipant pp WHERE pp.workspaceParticipant.id = :workspaceParticipantId)")
    void deleteTaskManager(Long workspaceParticipantId);
}
