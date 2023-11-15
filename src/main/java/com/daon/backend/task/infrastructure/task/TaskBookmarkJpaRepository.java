package com.daon.backend.task.infrastructure.task;

import com.daon.backend.task.domain.task.TaskBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskBookmarkJpaRepository extends JpaRepository<TaskBookmark, Long> {

    boolean existsTaskBookmarkByTaskIdAndParticipant_Id(Long taskId, Long projectParticipantId);
}
