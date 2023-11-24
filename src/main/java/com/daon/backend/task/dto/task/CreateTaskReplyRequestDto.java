package com.daon.backend.task.dto.task;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateTaskReplyRequestDto {

    @Size(max = 500)
    @NotBlank
    private String content;
}
