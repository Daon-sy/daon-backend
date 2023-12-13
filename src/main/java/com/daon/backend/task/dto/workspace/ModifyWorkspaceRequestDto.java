package com.daon.backend.task.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyWorkspaceRequestDto {

    @Size(max = 20)
    private String title;

    @Size(max = 100)
    private String description;

    private String imageUrl;

    @Size(max = 10)
    private String subject;

}
