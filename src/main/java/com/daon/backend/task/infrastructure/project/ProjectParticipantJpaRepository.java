package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.ProjectParticipant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectParticipantJpaRepository extends JpaRepository<ProjectParticipant, Long> {

    @EntityGraph(attributePaths = {"workspaceParticipant", "project"})
    @Override
    Optional<ProjectParticipant> findById(Long id);

    @EntityGraph(attributePaths = "workspaceParticipant")
    List<ProjectParticipant> findProjectParticipantsByProjectIdOrderByCreatedAtAsc(Long projectId);

    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId);
}
