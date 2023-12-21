package com.daon.backend.task.controller;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.workspace.FindMessageResponseDto;
import com.daon.backend.task.dto.workspace.MessageSummary;
import com.daon.backend.task.dto.workspace.SendMessageRequestDto;
import com.daon.backend.task.dto.workspace.SendMessageSummary;
import com.daon.backend.task.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.daon.backend.task.domain.authority.Authority.*;

@RequiredArgsConstructor
@RestController
public class MessageController {

    private final WorkspaceService workspaceService;

    @Operation(summary = "쪽지 보내기", description = "쪽지 생성 요청입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = MSG_CREATE)
    @PostMapping("/api/workspaces/{workspaceId}/messages")
    public void createMessage(@PathVariable Long workspaceId,
                            @RequestBody SendMessageRequestDto requestDto) {
        workspaceService.createMessage(workspaceId, requestDto);
    }

    @Operation(summary = "쪽지 단건 조회", description = "쪽지 단건 조회 요청입니다.")
    @CheckRole(authority = MSG_READ)
    @GetMapping("/api/workspaces/{workspaceId}/messages/{messageId}")
    public FindMessageResponseDto findMessage(@PathVariable Long workspaceId,
                                              @PathVariable Long messageId) {
        return workspaceService.findMessage(workspaceId, messageId);
    }

    @Operation(summary = "받은 쪽지 목록 조회", description = "받은 쪽지 목록 조회 요청입니다.")
    @CheckRole(authority = MSG_READ)
    @GetMapping("/api/workspaces/{workspaceId}/messages")
    public PageResponse<MessageSummary> findMessages(@PathVariable Long workspaceId,
                                                     @RequestParam(required = false) String target,
                                                     @RequestParam(required = false) String keyword,
                                                     @PageableDefault(size = 7) Pageable pageable) {
        return workspaceService.findMessages(workspaceId, target, keyword, pageable);
    }

    @Operation(summary = "보낸 쪽지 목록 조회", description = "보낸 쪽지 목록 조회 요청입니다.")
    @CheckRole(authority = MSG_READ)
    @GetMapping("/api/workspaces/{workspaceId}/send-messages")
    public PageResponse<SendMessageSummary> findSendMessages(@PathVariable Long workspaceId,
                                                             @RequestParam(required = false) String target,
                                                             @RequestParam(required = false) String keyword,
                                                             @PageableDefault(size = 7) Pageable pageable) {
        return workspaceService.findSendMessages(workspaceId, target, keyword, pageable);
    }

    @Operation(summary = "쪽지 삭제", description = "쪽지 삭제 요청입니다.")
    @CheckRole(authority = MSG_DELETE)
    @DeleteMapping("/api/workspaces/{workspaceId}/messages/{messageId}")
    public void deleteMessage(@PathVariable Long workspaceId,
                              @PathVariable Long messageId) {
        workspaceService.deleteMessage(workspaceId, messageId);
    }

    @Operation(summary = "쪽지 모두 읽기", description = "쪽지 모두 읽기 요청입니다.")
    @CheckRole(authority = MSG_READ)
    @PostMapping("/api/workspaces/{workspaceId}/messages/me")
    public void readAllMessages(@PathVariable Long workspaceId) {
        workspaceService.readAllMessages(workspaceId);
    }
}
