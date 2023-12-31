package com.daon.backend.task.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkspaceNoticeRequestDto {

    @Size(max=50)
    @NotBlank
    private String title;

    @Size(max=500)
    @NotBlank
    private String content;
}
