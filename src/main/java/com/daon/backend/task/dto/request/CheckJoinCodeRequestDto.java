package com.daon.backend.task.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CheckJoinCodeRequestDto {

    @NotBlank
    private String joinCode;
}
