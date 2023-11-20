package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.workspace.*;
import com.daon.backend.task.service.WorkspaceService;
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
@Tag(name = "Workspace", description = "Workspace domain API")
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Operation(summary = "워크스페이스 생성", description = "워크스페이스 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "워크스페이스 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CreateWorkspaceResponseDto createWorkspace(@RequestBody @Valid CreateWorkspaceRequestDto requestDto) {

        return workspaceService.createWorkspace(requestDto);
    }

    @Operation(summary = "워크스페이스 목록 조회", description = "워크스페이스 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 목록 조회 성공")
    })
    @GetMapping
    public FindWorkspacesResponseDto findWorkspaces() {

        return workspaceService.findWorkspaces();
    }

    @Operation(summary = "내 프로필 조회", description = "내 프로필 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 프로필 조회 성공")
    })
    @CheckRole(authority = PF_READ)
    @GetMapping("/{workspaceId}/participants/me")
    public FindProfileResponseDto findProfile(@PathVariable("workspaceId") Long workspaceId) {

        return workspaceService.findProfile(workspaceId);
    }

    @Operation(summary = "워크스페이스 참여자 목록 조회", description = "워크스페이스 참여자 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여자 목록 조회 성공")
    })
    @CheckRole(authority = WS_READ)
    @GetMapping("/{workspaceId}/participants")
    public FindWorkspaceParticipantsResponseDto findWorkspaceParticipants(@PathVariable("workspaceId") Long workspaceId) {

        return workspaceService.findWorkspaceParticipants(workspaceId);
    }

    @Operation(summary = "워크스페이스 참여자 권한 변경", description = "워크스페이스 참여자 권한 변경 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여자 권한 변경 성공")
    })
    @CheckRole(authority = WSP_ROLE_UPDATE)
    @PostMapping("/{workspaceId}/participants/role")
    public void modifyRole(@PathVariable("workspaceId") Long workspaceId,
                           @RequestBody @Valid ModifyRoleRequestDto requestDto) {
        workspaceService.modifyParticipantRole(requestDto, workspaceId);
    }

    @Operation(summary = "워크스페이스 수정", description = "워크스페이스 수정입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 수정 성공")
    })
    @CheckRole(authority = WS_UPDATE)
    @PatchMapping("/{workspaceId}")
    public void modifyWorkspace(@RequestBody ModifyWorkspaceRequestDto requestDto,
                                @PathVariable("workspaceId") Long workspaceId) {
        workspaceService.modifyWorkspace(requestDto, workspaceId);
    }

    @Operation(summary = "내 프로필 수정", description = "내 프로필 수정입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 프로필 수정 성공")
    })
    @CheckRole(authority = PF_READ)
    @PatchMapping("/{workspaceId}/participants/me")
    public void modifyProfile(@PathVariable("workspaceId") Long workspaceId,
                              @RequestBody @Valid ModifyProfileRequestDto requestDto) {
        workspaceService.modifyProfile(workspaceId, requestDto);
    }

    @Operation(summary = "워크스페이스 단건 조회", description = "워크스페이스 단건 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 단건 조회 성공")
    })
    @CheckRole(authority = WS_READ)
    @GetMapping("/{workspaceId}")
    public FindWorkspaceResponseDto findWorkspace(@PathVariable("workspaceId") Long workspaceId) {
        return workspaceService.findWorkspace(workspaceId);
    }

    @Operation(summary = "워크스페이스 참여자 초대", description = "워크스페이스 참여자 초대 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여자 초대 성공")
    })
    @CheckRole(authority = WSP_INVITE)
    @PostMapping("/{workspaceId}/invite")
    public void inviteMember(@PathVariable("workspaceId") Long workspaceId,
                             @RequestBody @Valid InviteMemberRequestDto requestDto) {
        workspaceService.inviteMember(workspaceId, requestDto);
    }

    @Operation(summary = "워크스페이스 참여", description = "워크스페이스 참여 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여 성공")
    })
    @PostMapping("/{workspaceId}/join")
    public void joinWorkspace(@PathVariable("workspaceId") Long workspaceId,
                              @RequestBody @Valid JoinWorkspaceRequestDto requestDto) {
        workspaceService.joinWorkspace(workspaceId, requestDto);
    }

    @Operation(summary = "워크스페이스 탈퇴", description = "워크스페이스 탈퇴 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 탈퇴 성공")
    })
    @CheckRole(authority = WS_READ)
    @DeleteMapping("/{workspaceId}/participants/me")
    public void withdrawWorkspace(@PathVariable("workspaceId") Long workspaceId) {
        workspaceService.withdrawWorkspace(workspaceId);
    }

    @Operation(summary = "워크스페이스 참여자 강퇴", description = "워크스페이스 참여자 강퇴 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여자 강퇴 성공")
    })
    @CheckRole(authority = WSP_DROP)
    @PostMapping("/{workspaceId}/participants/deportation")
    public void deportWorkspaceParticipant(@PathVariable("workspaceId") Long workspaceId,
                                           @RequestBody @Valid DeportWorkspaceParticipantRequestDto requestDto) {
        workspaceService.deportWorkspaceParticipant(workspaceId, requestDto);
    }

    @Operation(summary = "워크스페이스 삭제", description = "워크스페이스 삭제 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 삭제 성공")
    })
    @CheckRole(authority = WS_DELETE)
    @DeleteMapping("/{workspaceId}")
    public void deleteWorkspace(@PathVariable("workspaceId") Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
    }
}
