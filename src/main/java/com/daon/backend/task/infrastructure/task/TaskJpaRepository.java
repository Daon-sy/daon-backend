package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskJpaRepository extends JpaRepository<Task, Long> {

}

