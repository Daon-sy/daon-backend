package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.Workspace;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceJpaRepository extends JpaRepository<Workspace, Long> {

    @EntityGraph(attributePaths = "participants")
    Optional<Workspace> findWorkspaceByIdAndRemovedFalse(Long workspaceId);

    @Modifying
    @Query("UPDATE Task t " +
            "SET t.taskManager = null, t.creatorId = null, t.removed = true " +
            "WHERE t.project IN (SELECT p FROM Project p WHERE p.workspace.id = :workspaceId)")
    void deleteTasksRelatedWorkspace(Long workspaceId);

    @Modifying
    @Query("UPDATE Board b " +
            "SET b.removed = true " +
            "WHERE b.project IN (SELECT p FROM Project p WHERE p.workspace.id = :workspaceId)")
    void deleteBoardsRelatedWorkspace(Long workspaceId);

    @Modifying
    @Query("UPDATE Project p " +
            "SET p.removed = true " +
            "WHERE p.workspace.id = :workspaceId")
    void deleteProjectsRelatedWorkspace(Long workspaceId);

    @Modifying
    @Query("UPDATE Task t " +
            "SET t.taskManager = null " +
            "WHERE t.project IN (SELECT p FROM Project p WHERE p.workspace.id = :workspaceId)")
    void deleteTaskRepliesRelatedWorkspace(Long workspaceId);

    @Modifying
    @Query("UPDATE Task t " +
            "SET t.taskManager = null " +
            "WHERE t.taskManager IN (SELECT pp FROM ProjectParticipant pp " +
            "                        WHERE pp.workspaceParticipant.id = :workspaceParticipant)")
    void deleteTaskManagerRelatedWorkspaceParticipant(Long workspaceParticipant);

    @Modifying
    @Query("UPDATE TaskReply t " +
            "SET t.taskReplyWriter = null " +
            "WHERE t.taskReplyWriter IN (SELECT pp FROM ProjectParticipant pp" +
            "                            WHERE pp.workspaceParticipant.id = :workspaceParticipantId " +
            "                               AND pp.memberId = :memberId)")
    void deleteAllReplyWriterRelatedMemberId(Long workspaceParticipantId, String memberId);
}
