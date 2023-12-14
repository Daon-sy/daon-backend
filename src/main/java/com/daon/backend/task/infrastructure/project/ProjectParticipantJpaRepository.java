package com.daon.backend.task.infrastructure.project;

import com.daon.backend.task.domain.project.ProjectParticipant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectParticipantJpaRepository extends JpaRepository<ProjectParticipant, Long> {

    @EntityGraph(attributePaths = {"workspaceParticipant", "project"})
    @Override
    Optional<ProjectParticipant> findById(Long id);

    Optional<ProjectParticipant> findProjectParticipantByProjectIdAndMemberId(Long projectId, String memberId);
}
