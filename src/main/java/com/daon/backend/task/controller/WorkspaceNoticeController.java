package com.daon.backend.task.controller;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeResponseDto;
import com.daon.backend.task.dto.workspace.FindWorkspaceNoticeResponseDto;
import com.daon.backend.task.dto.workspace.ModifyWorkspaceNoticeRequestDto;
import com.daon.backend.task.service.WorkspaceNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "WorkspaceNotice", description = "WorkspaceNotice domain API")
public class WorkspaceNoticeController {

    private final WorkspaceNoticeService workspaceNoticeService;

    @Operation(summary = "워크스페이스 공지사항 생성", description = "워크스페이스 공지사항 생성 요청입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = Authority.WSN_CREATE)
    @PostMapping("/api/workspaces/{workspaceId}/notices")
    public CreateWorkspaceNoticeResponseDto createWorkspaceNotice(@PathVariable Long workspaceId,
                                                                  @RequestBody CreateWorkspaceNoticeRequestDto requestDto){
        return workspaceNoticeService.createWorkspaceNotice(workspaceId, requestDto);
    }

    @Operation(summary = "워크스페이스 공지사항 목록 조회", description = "워크스페이스 공지사항 목록 조회 요청입니다.")
    @CheckRole(authority = Authority.WSN_READ)
    @GetMapping("/api/workspaces/{workspaceId}/notices")
    public PageResponse<WorkspaceNoticeSummary> findWorkspaceNotices(
            @PathVariable Long workspaceId,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 4) Pageable pageable) {
        return workspaceNoticeService.findWorkspaceNotices(workspaceId, keyword, pageable);
    }

    @Operation(summary = "워크스페이스 공지사항 단건 조회", description = "워크스페이스 공지사항 단건 조회 요청입니다.")
    @CheckRole(authority = Authority.WSN_READ)
    @GetMapping("/api/workspaces/{workspaceId}/notices/{noticeId}")
    public FindWorkspaceNoticeResponseDto findWorkspaceNotice(@PathVariable Long noticeId){
        return workspaceNoticeService.findWorkspaceNotice(noticeId);
    }

    @Operation(summary = "워크스페이스 공지사항 수정", description = "워크스페이스 공지사항 수정 요청입니다.")
    @CheckRole(authority = Authority.WSN_UPDATE)
    @PatchMapping("/api/workspaces/{workspaceId}/notices/{noticeId}")
    public void modifyWorkspaceNotice(@PathVariable Long workspaceId,
                                      @PathVariable Long noticeId,
                                      @RequestBody @Valid ModifyWorkspaceNoticeRequestDto requestDto) {
        workspaceNoticeService.modifyWorkspaceNoticeContent(workspaceId, noticeId, requestDto);
    }

    @Operation(summary = "워크스페이스 공지사항 삭제", description = "워크스페이스 공지사항 삭제 요청입니다.")
    @CheckRole(authority = Authority.WSN_DELETE)
    @DeleteMapping("/api/workspaces/{workspaceId}/notices/{noticeId}")
    public void deleteWorkspaceNotice(@PathVariable Long workspaceId,
                                      @PathVariable Long noticeId){
        workspaceNoticeService.deleteWorkspaceNotice(workspaceId, noticeId);
    }
}
