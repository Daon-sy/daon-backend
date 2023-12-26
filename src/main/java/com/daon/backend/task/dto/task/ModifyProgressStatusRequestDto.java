package com.daon.backend.task.dto.task;

import com.daon.backend.task.domain.task.TaskProgressStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProgressStatusRequestDto {

    private TaskProgressStatus progressStatus;
}
