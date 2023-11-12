package com.daon.backend.task.dto.request;

import com.daon.backend.task.domain.project.TaskProgressStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class ModifyTaskRequestDto {

    private String title;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private boolean emergency;

    private TaskProgressStatus progressStatus;

    private Long boardId;

    private Long workspaceParticipantId;
}
