package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.project.*;
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

import static com.daon.backend.task.domain.authority.Authority.*;

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
    public CreateProjectResponseDto createProject(@PathVariable Long workspaceId,
                                                  @RequestBody @Valid CreateProjectRequestDto requestDto) {
        return projectService.createProject(workspaceId, requestDto);
    }

    @Operation(summary = "프로젝트 목록 조회", description = "프로젝트 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 목록 조회 성공")
    })
    @CheckRole(authority = PJ_READ)
    @GetMapping
    public FindProjectsResponseDto findProjects(@PathVariable Long workspaceId) {

        return projectService.findAllProjectInWorkspace(workspaceId);
    }

    @Operation(summary = "프로젝트 초대", description = "프로젝트 초대 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 초대 성공")
    })
    @CheckRole(authority = PJ_CREATE)
    @PostMapping("/{projectId}/invite")
    public void inviteWorkspaceParticipant(@PathVariable("projectId") Long projectId,
                                           @RequestBody @Valid InviteWorkspaceParticipantRequestDto requestDto) {
        projectService.inviteWorkspaceParticipant(projectId, requestDto);
    }

    @Operation(summary = "프로젝트 참여자 목록 조회", description = "프로젝트 참여자 목록 조회입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 참여자 목록 조회 성공")
    })
    @CheckRole(authority = PJ_READ)
    @GetMapping("/{projectId}/participants")
    public FindProjectParticipantsResponseDto findProjectParticipants(@PathVariable("projectId") Long projectId) {
        return projectService.findProjectParticipants(projectId);
    }

    @Operation(summary = "프로젝트 수정", description = "프로젝트 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로젝트 수정 성공")
    })
    @CheckRole(authority = PJ_UPDATE)
    @PatchMapping("/{projectId}")
    public void modifyProject(@PathVariable("projectId") Long projectId,
                              @RequestBody @Valid ModifyProjectRequestDto requestDto) {
        projectService.modifyProject(projectId, requestDto);
    }
}
