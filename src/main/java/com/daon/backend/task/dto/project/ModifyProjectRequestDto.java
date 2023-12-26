package com.daon.backend.task.dto.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProjectRequestDto {

    @Size(max = 20)
    private String title;

    @Size(max = 100)
    private String description;
}
