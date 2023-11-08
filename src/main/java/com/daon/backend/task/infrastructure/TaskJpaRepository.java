package com.daon.backend.task.infrastructure;

import com.daon.backend.task.domain.project.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {

}

