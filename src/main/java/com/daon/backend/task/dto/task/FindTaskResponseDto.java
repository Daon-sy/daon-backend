package com.daon.backend.task.dto.task;

import com.daon.backend.task.dto.TaskDetail;
import lombok.Getter;

@Getter
public class FindTaskResponseDto extends TaskDetail {

    public FindTaskResponseDto(TaskDetail taskDetail) {
        super(
                taskDetail.getTaskId(), taskDetail.getProject(),
                taskDetail.getBoard(), taskDetail.getTaskManager(),
                taskDetail.getTitle(), taskDetail.getContent(),
                taskDetail.getStartDate(), taskDetail.getEndDate(),
                taskDetail.getProgressStatus(), taskDetail.isEmergency(),
                taskDetail.isBookmark(), taskDetail.getCreatedAt(), taskDetail.getModifiedAt()
        );
    }
}
