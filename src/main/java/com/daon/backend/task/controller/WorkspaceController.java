package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.request.*;
import com.daon.backend.task.dto.response.*;
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
    public CommonResponse<CreateWorkspaceResponseDto> createWorkspace(@RequestBody @Valid CreateWorkspaceRequestDto requestDto) {
        CreateWorkspaceResponseDto result = workspaceService.createWorkspace(requestDto);

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "워크스페이스 목록 조회", description = "워크스페이스 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 목록 조회 성공")
    })
    @GetMapping
    public CommonResponse<WorkspaceListResponseDto> findWorkspaces() {
        WorkspaceListResponseDto result = workspaceService.findAllWorkspace();

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "프로필 조회", description = "그룹 워크스페이스 내 프로필 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공")
    })
    @CheckRole(authority = WS_READ)
    @GetMapping("/{workspaceId}/participants/me")
    public CommonResponse<FindProfileResponseDto> findProfile(@PathVariable("workspaceId") Long workspaceId) {
        FindProfileResponseDto result = workspaceService.findProfile(workspaceId);

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "워크스페이스 참여자 목록 조회", description = "워크스페이스 참여자 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여자 목록 조회 성공")
    })
    @CheckRole(authority = WS_READ)
    @GetMapping("/{workspaceId}/participants")
    public CommonResponse<FindWorkspaceParticipantsResponseDto> findWorkspaceParticipants(@PathVariable("workspaceId") Long workspaceId) {
        FindWorkspaceParticipantsResponseDto result = workspaceService.findWorkspaceParticipants(workspaceId);

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "워크스페이스 참여자 권한 변경", description = "워크스페이스 참여자 권한 변경 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여자 권한 변경 성공")
    })
    @CheckRole(authority = WSP_ROLE_UPDATE)
    @PatchMapping("/{workspaceId}/participants/role")
    public CommonResponse<Void> modifyRole(@PathVariable("workspaceId") Long workspaceId,
                                           @RequestBody @Valid ModifyRoleRequestDto requestDto) {
        workspaceService.modifyParticipantRole(requestDto, workspaceId);

        return CommonResponse.createSuccess(null);
    }

    // TODO 워크스페이스 참여자 초대 API 수정 필요
    @Operation(summary = "회원 초대", description = "회원 초대 요청입니다. (워크스페이스 참여자로 등록)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 초대 성공")
    })
    @CheckRole(authority = WSP_INVITE)
    @PostMapping("/{workspaceId}/invite")
    public CommonResponse<Void> inviteMember(@PathVariable("workspaceId") Long workspaceId,
                                             @RequestBody InviteMemberRequestDto requestDto) {
        workspaceService.inviteMember(workspaceId, requestDto);

        return CommonResponse.createSuccess(null);
    }

    @Operation(summary = "워크스페이스 수정", description = "워크스페이스 수정입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 수정 성공")
    })
    @CheckRole(authority = WS_UPDATE)
    @PatchMapping("/{workspaceId}")
    public CommonResponse<Void> modifyWorkspace(@RequestBody ModifyWorkspaceRequestDto requestDto ,
                                                @PathVariable("workspaceId") Long workspaceId){
        workspaceService.modifyWorkspace(requestDto, workspaceId);

        return CommonResponse.createSuccess(null);
    }
}
