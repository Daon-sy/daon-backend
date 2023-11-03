package com.daon.backend.task.controller;

import com.daon.backend.common.response.ApiResponse;
import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import com.daon.backend.task.dto.response.CreateProjectResponseDto;
import com.daon.backend.task.dto.response.ProjectListResponseDto;
import com.daon.backend.task.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ApiResponse<CreateProjectResponseDto> createProject(
            @PathVariable Long workspaceId,
            @RequestBody @Valid CreateProjectRequestDto requestDto
    ) {
        Long projectId = projectService.createProject(workspaceId, requestDto);
        return ApiResponse.createSuccess(new CreateProjectResponseDto(projectId));
    }

    @GetMapping
    public ApiResponse<ProjectListResponseDto> projectList(@PathVariable Long workspaceId) {
        ProjectListResponseDto projectListResponseDto = projectService.findAllProjectInWorkspace(workspaceId);
        return ApiResponse.createSuccess(projectListResponseDto);
    }
}
