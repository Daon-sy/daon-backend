package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.task.domain.workspace.Message;
import com.daon.backend.task.domain.workspace.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageJpaRepository extends JpaRepository<Message, Long> {

    Page<Message> findAllByWorkspaceAndReceiverIdOrderByCreatedAtDesc(Workspace workspace, Long receiverId, Pageable pageable);
}
