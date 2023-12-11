package com.daon.backend.task.dto.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyProfileRequestDto {

    @Size(max = 20)
    private String name;

    private String imageUrl;

    private String email;
}
