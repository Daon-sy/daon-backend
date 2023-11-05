package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJpaRepository extends JpaRepository<Project, Long> {
}
