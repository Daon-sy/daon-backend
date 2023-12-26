package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<Message, Long> {
}
