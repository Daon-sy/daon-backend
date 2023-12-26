package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = "workspace")
    Optional<Project> findProjectById(Long projectId);

    @Modifying
    @Query("UPDATE Task t " +
            "SET t.taskManager = null " +
            "WHERE t.taskManager IN (SELECT pp FROM ProjectParticipant pp WHERE pp.id = :projectParticipantId)")
    void deleteTaskManager(Long projectParticipantId);
}
