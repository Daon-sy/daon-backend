package com.daon.backend.task.dto.request;

import com.daon.backend.task.domain.project.TaskProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProgressStatusRequestDto {

    private TaskProgressStatus progressStatus;
}
