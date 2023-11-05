package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import com.daon.backend.task.dto.response.CreateProjectResponseDto;
import com.daon.backend.task.dto.response.ProjectListResponseDto;
import com.daon.backend.task.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Project", description = "Project domain API")
@RequestMapping("/api/workspaces/{workspaceId}/projects")
public class ProjectController {

    private final ProjectService projectService;

    @Operation(summary = "프로젝트 생성", description = "프로젝트 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "프로젝트 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResponse<CreateProjectResponseDto> createProject(
            @PathVariable Long workspaceId,
            @RequestBody @Valid CreateProjectRequestDto requestDto
    ) {
        Long projectId = projectService.createProject(workspaceId, requestDto);
        return CommonResponse.createSuccess(new CreateProjectResponseDto(projectId));
    }

    @Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 목록 조회 성공")
    })
    @GetMapping
    public CommonResponse<ProjectListResponseDto> projectList(@PathVariable Long workspaceId) {
        ProjectListResponseDto projectListResponseDto = projectService.findAllProjectInWorkspace(workspaceId);
        return CommonResponse.createSuccess(projectListResponseDto);
    }
}
