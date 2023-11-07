package com.daon.backend.task.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateBoardRequestDto {

    @NotBlank
    private String title;
}
