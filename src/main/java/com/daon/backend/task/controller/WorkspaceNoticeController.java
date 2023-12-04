package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeResponseDto;
import com.daon.backend.task.service.WorkspaceNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
}
