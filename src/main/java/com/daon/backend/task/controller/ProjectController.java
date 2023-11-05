package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import com.daon.backend.task.dto.response.CreateProjectResponseDto;
import com.daon.backend.task.dto.response.ProjectListResponseDto;
import com.daon.backend.task.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/projects")
public class ProjectController {

    private final ProjectService projectService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResponse<CreateProjectResponseDto> createProject(
            @PathVariable Long workspaceId,
            @RequestBody @Valid CreateProjectRequestDto requestDto
    ) {
        Long projectId = projectService.createProject(workspaceId, requestDto);
        return CommonResponse.createSuccess(new CreateProjectResponseDto(projectId));
    }

    @GetMapping
    public CommonResponse<ProjectListResponseDto> projectList(@PathVariable Long workspaceId) {
        ProjectListResponseDto projectListResponseDto = projectService.findAllProjectInWorkspace(workspaceId);
        return CommonResponse.createSuccess(projectListResponseDto);
    }
}
