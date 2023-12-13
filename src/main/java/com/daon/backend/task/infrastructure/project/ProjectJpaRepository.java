package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {

    @EntityGraph(attributePaths = {"workspace", "tasks"})
    Optional<Project> findProjectByIdAndRemovedFalse(Long projectId);

    @Modifying
    @Query("UPDATE Task t " +
            "SET t.taskManager = NULL " +
            "WHERE t.taskManager IN (SELECT pp FROM ProjectParticipant pp WHERE pp.memberId = :memberId) " +
            "AND t.project.id = :projectId")
    void deleteTaskManagerRelatedProjectByMemberId(Long projectId, String memberId);

    @Modifying
    @Query("UPDATE TaskReply t " +
            "SET t.taskReplyWriter = NULL " +
            "WHERE t.taskReplyWriter IN (SELECT pp FROM ProjectParticipant pp WHERE pp.memberId = :memberId " +
            "AND t.task.project.id = :projectId)")
    void deleteReplyWriterRelatedProjectByMemberId(Long projectId, String memberId);

    @Modifying
    @Query("UPDATE TaskReply t " +
            "SET t.taskReplyWriter = NULL " +
            "WHERE t.taskReplyWriter IN (SELECT pp FROM ProjectParticipant pp WHERE pp.id = :projectParticipantId)")
    void deleteReplyWriterRelatedProjectParticipant(Long projectParticipantId);

    @Modifying
    @Query("UPDATE TaskReply t " +
            "SET t.taskReplyWriter = NULL " +
            "WHERE t.taskReplyWriter IN (SELECT pp FROM ProjectParticipant pp WHERE pp.project.id = :projectId)")
    void deleteReplyWriterRelatedProject(Long projectId);
}
