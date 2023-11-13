package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.request.CreateProjectRequestDto;
import com.daon.backend.task.dto.request.InviteWorkspaceParticipantRequestDto;
import com.daon.backend.task.dto.response.CreateProjectResponseDto;
import com.daon.backend.task.dto.response.FindProjectParticipantsResponseDto;
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

import static com.daon.backend.task.domain.authority.Authority.PJ_CREATE;
import static com.daon.backend.task.domain.authority.Authority.PJ_READ;

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
    @CheckRole(authority = PJ_CREATE)
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
    @CheckRole(authority = PJ_READ)
    @GetMapping
    public CommonResponse<ProjectListResponseDto> findProjects(@PathVariable Long workspaceId) {
        ProjectListResponseDto projectListResponseDto = projectService.findAllProjectInWorkspace(workspaceId);
        return CommonResponse.createSuccess(projectListResponseDto);
    }

    @Operation(summary = "프로젝트 초대", description = "프로젝트 초대 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 초대 성공")
    })
    @CheckRole(authority = PJ_CREATE)
    @PostMapping("/{projectId}/invite")
    public CommonResponse<Void> inviteWorkspaceParticipant(@PathVariable("workspaceId") Long workspaceId,
                                                           @PathVariable("projectId") Long projectId,
                                                           @RequestBody InviteWorkspaceParticipantRequestDto requestDto) {
        projectService.inviteWorkspaceParticipant(projectId, requestDto);

        return CommonResponse.createSuccess(null);
    }

    @Operation(summary = "프로젝트 구성원 목록 조회", description = "프로젝트 구성원 목록 조회입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 구성원 목록 조회 성공")
    })
    @CheckRole(authority = PJ_READ)
    @GetMapping("/{projectId}/participants")
    public CommonResponse<FindProjectParticipantsResponseDto> findProjectParticipants(@PathVariable("workspaceId") Long workspaceId,
                                                                                      @PathVariable("projectId") Long projectId) {
        FindProjectParticipantsResponseDto result = projectService.findProjectParticipants(projectId);

        return CommonResponse.createSuccess(result);
    }
}
