package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"workspace", "tasks"})
    Optional<Project> findProjectByIdAndRemovedFalse(Long projectId);

    @EntityGraph(attributePaths = "participants")
    Optional<Project> findProjectWithParticipantsByIdAndRemovedFalse(Long projectId);

    @EntityGraph(attributePaths = "boards")
    Optional<Project> findProjectWithBoardsByIdAndRemovedFalse(Long projectId);

    @EntityGraph(attributePaths = "tasks")
    Optional<Project> findProjectWithTasksByIdAndRemovedFalse(Long projectId);

    List<Project> findAllProjectsByWorkspaceId(Long workspaceId);

    @Modifying
    @Query("UPDATE Task t " +
            "SET t.taskManager = NULL " +
            "WHERE t.taskManager IN (SELECT pp FROM ProjectParticipant pp WHERE pp.memberId = :memberId) " +
            "AND t.project.id = :projectId")
    void deleteTaskManagerRelatedProjectByMemberId(Long projectId, String memberId);
}
