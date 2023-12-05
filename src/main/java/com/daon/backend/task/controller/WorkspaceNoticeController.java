package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.workspace.*;
import com.daon.backend.task.service.WorkspaceNoticeService;
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
@Tag(name = "WorkspaceNotice", description = "WorkspaceNotice domain API")
@RequestMapping("/api/workspaces/{workspaceId}/notices")
public class WorkspaceNoticeController {

    private final WorkspaceNoticeService workspaceNoticeService;

    @Operation(summary = "워크스페이스 공지사항 생성", description = "워크스페이스 공지사항 생성 요청입니다.")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201", description = "워크스페이스 공지사항 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = Authority.WSN_CREATE)
    @PostMapping
    public CreateWorkspaceNoticeResponseDto createWorkspaceNotice(@PathVariable Long workspaceId,
                                                                  @RequestBody CreateWorkspaceNoticeRequestDto requestDto){
        return workspaceNoticeService.createWorkspaceNotice(workspaceId, requestDto);
    }

    @Operation(summary = "워크스페이스 공지사항 목록 조회", description = "워크스페이스 공지사항 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 공지사항 목록 조회 성공")
    })
    @CheckRole(authority = Authority.WSN_READ)
    @GetMapping
    public FindWorkspaceNoticesResponseDto findWorkspaceNotices(@PathVariable Long workspaceId){
        return workspaceNoticeService.findWorkspaceNotices(workspaceId);
    }

    @Operation(summary = "워크스페이스 공지사항 단건 조회", description = "워크스페이스 공지사항 단건 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 공지사항 단건 조회 성공")
    })
    @CheckRole(authority = Authority.WSN_READ)
    @GetMapping("/{noticeId}")
    public FindWorkspaceNoticeResponseDto findWorkspaceNotice(@PathVariable Long noticeId){
        return workspaceNoticeService.findWorkspaceNotice(noticeId);
    }

    @Operation(summary = "워크스페이스 공지사항 수정", description = "워크스페이스 공지사항 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 공지사항 수정 성공")
    })
    @CheckRole(authority = Authority.WSN_UPDATE)
    @PatchMapping("/{noticeId}")
    public void modifyWorkspaceNotice(@PathVariable Long workspaceId,
                                      @PathVariable Long noticeId,
                                      @RequestBody @Valid ModifyWorkspaceNoticeRequestDto requestDto) {
        workspaceNoticeService.modifyWorkspaceNoticeContent(workspaceId, noticeId, requestDto);
    }

    @Operation(summary = "워크스페이스 공지사항 삭제", description = "워크스페이스 공지사항 삭제 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "워크스페이스 공지사항 삭제 성공")
    })
    @CheckRole(authority = Authority.WSN_DELETE)
    @DeleteMapping("/{noticeId}")
    public void deleteWorkspaceNotice(@PathVariable Long noticeId){
        workspaceNoticeService.deleteWorkspaceNotice(noticeId);
    }
}
