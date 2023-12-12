package com.daon.backend.task.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBoardRequestDto {

    @NotBlank
    @Size(max = 20)
    private String title;
}
