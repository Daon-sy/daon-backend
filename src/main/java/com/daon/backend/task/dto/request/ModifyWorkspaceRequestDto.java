package com.daon.backend.task.dto.request;

import com.daon.backend.task.domain.workspace.Division;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyWorkspaceRequestDto {

    private String title;

    private String description;

    private String imageUrl;

    private String subject;

}
