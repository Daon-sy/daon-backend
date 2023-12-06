package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.workspace.FindMessageResponseDto;
import com.daon.backend.task.dto.workspace.SendMessageRequestDto;
import com.daon.backend.task.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.daon.backend.task.domain.authority.Authority.WS_READ;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final WorkspaceService workspaceService;

    @Operation(summary = "쪽지 보내기", description = "쪽지 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "쪽지 보내기 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = WS_READ)
    @PostMapping("/api/workspaces/{workspaceId}/messages")
    public void createMessage(@PathVariable Long workspaceId,
                            @RequestBody SendMessageRequestDto requestDto) {
        workspaceService.createMessage(workspaceId, requestDto);
    }

    @Operation(summary = "쪽지 단건 조회", description = "쪽지 단건 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쪽지 단건 조회 성공")
    })
    @GetMapping("/api/workspaces/{workspaceId}/messages/{messageId}")
    public FindMessageResponseDto findMessage(@PathVariable Long workspaceId,
                                              @PathVariable Long messageId) {
        return workspaceService.findMessage(workspaceId, messageId);
    }

    @Operation(summary = "쪽지 삭제", description = "쪽지 삭제 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "쪽지 삭제 성공")
    })
    @DeleteMapping("/api/workspaces/{workspaceId}/messages/{messageId}")
    public void deleteMessage(@PathVariable Long workspaceId,
                              @PathVariable Long messageId) {
        workspaceService.deleteMessage(workspaceId, messageId);
    }
}
