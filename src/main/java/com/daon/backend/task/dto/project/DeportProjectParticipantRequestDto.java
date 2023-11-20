package com.daon.backend.task.dto.project;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class DeportProjectParticipantRequestDto {

    @NotNull
    private Long projectParticipantId;
}
