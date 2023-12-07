package com.daon.backend.task.dto.project;

import com.daon.backend.task.domain.project.ProjectParticipant;
import lombok.Getter;

@Getter
public class FindMyProfileResponseDto extends FindProjectParticipantsResponseDto.ProjectParticipantProfile {

    public FindMyProfileResponseDto(ProjectParticipant projectParticipant) {
        super(projectParticipant);
    }
}
