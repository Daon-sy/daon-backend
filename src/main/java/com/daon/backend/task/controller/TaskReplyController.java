package com.daon.backend.task.controller;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.TaskReplySummary;
import com.daon.backend.task.dto.task.CreateTaskReplyRequestDto;
import com.daon.backend.task.dto.task.CreateTaskReplyResponseDto;
import com.daon.backend.task.dto.task.ModifyTaskReplyRequestDto;
import com.daon.backend.task.service.TaskReplyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.daon.backend.task.domain.authority.Authority.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Reply", description = "Reply domain API")
@RequestMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}/reply")
public class TaskReplyController {

    private final TaskReplyService taskReplyService;

    @Operation(summary = "할일 댓글 생성", description = "할일 댓글 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "할일 댓글 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = REP_CREATE)
    @PostMapping
    public CreateTaskReplyResponseDto createTaskReply(@PathVariable Long projectId,
                                                      @PathVariable Long taskId,
                                                      @RequestBody @Valid CreateTaskReplyRequestDto requestDto) {
        return taskReplyService.createTaskReply(projectId, taskId, requestDto);
    }

    @Operation(summary = "할일 댓글 목록 조회", description = "할일 댓글 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할일 댓글 목록 조회 성공")
    })
    @CheckRole(authority = REP_READ)
    @GetMapping
    public SliceResponse<TaskReplySummary> findTaskReplies(@PathVariable Long projectId,
                                                           @PathVariable Long taskId,
                                                           @PageableDefault Pageable pageable) {
        return taskReplyService.findTaskReplies(projectId, taskId, pageable);
    }

    @Operation(summary = "할일 댓글 수정", description = "할일 댓글 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할일 댓글 수정 성공")
    })
    @CheckRole(authority = REP_UPDATE)
    @PutMapping("/{taskReplyId}")
    public void modifyTaskReply(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long taskReplyId,
                                @RequestBody @Valid ModifyTaskReplyRequestDto requestDto) {
        taskReplyService.modifyTaskReplyContent(projectId, taskId, taskReplyId, requestDto);
    }

    @Operation(summary = "할일 댓글 삭제", description = "할일 댓글 삭제 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할일 댓글 삭제 성공")
    })
    @CheckRole(authority = REP_DELETE)
    @DeleteMapping("/{taskReplyId}")
    public void deleteTaskReply(@PathVariable Long projectId,
                                @PathVariable Long taskId,
                                @PathVariable Long taskReplyId) {
        taskReplyService.deleteTaskReply(projectId, taskId, taskReplyId);
    }
}
