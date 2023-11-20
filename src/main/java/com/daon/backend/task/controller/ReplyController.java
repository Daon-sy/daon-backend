package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.task.CreateReplyRequestDto;
import com.daon.backend.task.dto.task.CreateReplyResponseDto;
import com.daon.backend.task.dto.task.FindRepliesResponseDto;
import com.daon.backend.task.dto.task.ModifyReplyRequestDto;
import com.daon.backend.task.service.ReplyService;
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
@Tag(name = "Reply", description = "Reply domain API")
@RequestMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}/reply")
public class ReplyController {

    private ReplyService replyService;

    @Operation(summary = "댓글 생성", description = "댓글 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = REP_CREATE)
    @PostMapping
    public CreateReplyResponseDto createReply(@PathVariable("projectId") Long projectId,
                                              @PathVariable("taskId") Long taskId,
                                              @RequestBody @Valid CreateReplyRequestDto requestDto) {
        return replyService.createReply(projectId, taskId, requestDto);
    }

    @Operation(summary = "댓글 목록 조회", description = "댓글 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공")
    })
    @CheckRole(authority = REP_READ)
    @GetMapping
    public FindRepliesResponseDto findReplies(@PathVariable("projectId") Long projectId,
                                              @PathVariable("taskId") Long taskId) {
        return replyService.findReplies(projectId, taskId);
    }

    @Operation(summary = "댓글 수정", description = "댓글 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공")
    })
    @CheckRole(authority = REP_UPDATE)
    @PutMapping("/{replyId}")
    public void modifyReply(@PathVariable("projectId") Long projectId,
                            @PathVariable("taskId") Long taskId,
                            @PathVariable("replyId") Long replyId,
                            @RequestBody @Valid ModifyReplyRequestDto requestDto) {
        replyService.modifyReply(projectId, taskId, replyId, requestDto);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 삭제 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공")
    })
    @CheckRole(authority = REP_DELETE)
    @DeleteMapping("/{replyId}")
    public void deleteReply(@PathVariable("projectId") Long projectId,
                            @PathVariable("taskId") Long taskId,
                            @PathVariable("replyId") Long replyId) {
        replyService.deleteReply(projectId, taskId, replyId);
    }


}
