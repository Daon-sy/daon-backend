package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.request.CheckJoinCodeRequestDto;
import com.daon.backend.task.dto.request.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.request.InviteMemberRequestDto;
import com.daon.backend.task.dto.request.JoinWorkspaceRequestDto;
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

import static com.daon.backend.task.domain.authority.Authority.WSP_INVITE;
import static com.daon.backend.task.domain.authority.Authority.WS_READ;
import static com.daon.backend.task.domain.authority.CheckRole.MembershipType.WORKSPACE;

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
    public CommonResponse<CreateWorkspaceResponseDto> createWorkspace(
            @RequestBody @Valid CreateWorkspaceRequestDto requestDto
    ) {
        Long workspaceId = workspaceService.createWorkspace(requestDto);
        return CommonResponse.createSuccess(new CreateWorkspaceResponseDto(workspaceId));
    }

    @Operation(summary = "워크스페이스 목록 조회", description = "워크스페이스 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 목록 조회 성공")
    })
    @GetMapping
    public CommonResponse<WorkspaceListResponseDto> findWorkspaces() {
        WorkspaceListResponseDto workspaceListResponseDto = workspaceService.findAllWorkspace();
        return CommonResponse.createSuccess(workspaceListResponseDto);
    }

    @Operation(summary = "참여코드 확인", description = "참여코드 확인 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "참여코드 확인 성공")
    })
    @CheckRole(authority = WSP_INVITE, membership = WORKSPACE)
    @PostMapping("/code")
    public CommonResponse<Void> checkJoinCode(@RequestBody @Valid CheckJoinCodeRequestDto requestDto) {
        workspaceService.checkJoinCode(requestDto);

        return CommonResponse.createSuccess(null);
    }

    @Operation(summary = "워크스페이스 참여", description = "워크스페이스 참여 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 참여성공")
    })
    @PostMapping("/join")
    public CommonResponse<JoinWorkspaceResponseDto> joinWorkspace(@RequestBody @Valid JoinWorkspaceRequestDto requestDto) {
        JoinWorkspaceResponseDto result = workspaceService.joinWorkspace(requestDto);

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "프로필 조회", description = "그룹 워크스페이스 내 프로필 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 조회 성공")
    })
    @CheckRole(authority = WS_READ, membership = WORKSPACE)
    @GetMapping("/{workspaceId}/profile/me")
    public CommonResponse<FindProfileResponseDto> findProfile(@PathVariable("workspaceId") Long workspaceId) {
        FindProfileResponseDto result = workspaceService.findProfile(workspaceId);

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "워크스페이스 구성원 목록 조회", description = "워크스페이스 구성원 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 구성원 목록 조회 성공")
    })
    @CheckRole(authority = WS_READ, membership = WORKSPACE)
    @GetMapping("/{workspaceId}/participants")
    public CommonResponse<FindParticipantsResponseDto> findParticipants(@PathVariable("workspaceId") Long workspaceId) {
        FindParticipantsResponseDto result = workspaceService.findParticipants(workspaceId);

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "회원 초대", description = "회원 초대 요청입니다. (워크스페이스 참여자로 등록)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 초대 성공")
    })
    @CheckRole(authority = WSP_INVITE, membership = WORKSPACE)
    @PostMapping("/{workspaceId}/invite")
    public CommonResponse<Void> inviteMember(@PathVariable("workspaceId") Long workspaceId,
                                             @RequestBody InviteMemberRequestDto requestDto) {
        workspaceService.inviteMember(workspaceId, requestDto);

        return CommonResponse.createSuccess(null);
    }
}
