package com.daon.backend.task.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class CreateBoardRequestDto {

    @NotBlank
    @Size(max = 20)
    private String title;
}
