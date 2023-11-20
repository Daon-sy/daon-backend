package com.daon.backend.task.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class ModifyTaskReplyRequestDto {

    @NotBlank
    @Size(max = 500)
    private String content;

}

