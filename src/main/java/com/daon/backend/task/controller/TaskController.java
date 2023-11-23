package com.daon.backend.task.controller;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.task.*;
import com.daon.backend.task.dto.task.history.TaskHistory;
import com.daon.backend.task.dto.task.history.TaskHistoryResponseDto;
import com.daon.backend.task.service.TaskService;
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
@Tag(name = "Task", description = "Task domain API")
@RequestMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "할 일 생성", description = "할 일 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "할 일 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = TSK_CREATE)
    @PostMapping
    public CreateTaskResponseDto createTask(@PathVariable("projectId") Long projectId,
                                            @RequestBody @Valid CreateTaskRequestDto requestDto) {
        return taskService.createTask(projectId, requestDto);
    }

    @Operation(summary = "할 일 수정", description = "할 일 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 수정 성공")
    })
    @CheckRole(authority = TSK_UPDATE)
    @PutMapping("/{taskId}")
    public void modifyTask(@PathVariable("projectId") Long projectId,
                           @PathVariable("taskId") Long taskId,
                           @RequestBody @Valid ModifyTaskRequestDto requestDto) {
        taskService.modifyTask(projectId, taskId, requestDto);
    }

    @Operation(summary = "할 일 단건 조회", description = "할 일 단건 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 단건 조회 성공")
    })
    @CheckRole(authority = TSK_READ)
    @GetMapping("/{taskId}")
    public FindTaskResponseDto findTask(@PathVariable("taskId") Long taskId) {
        return taskService.findTask(taskId);
    }

    @Operation(summary = "할 일 진행 상태 변경", description = "할 일 진행 상태 변경 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 진행 상태 변경 성공")
    })
    @CheckRole(authority = TSK_UPDATE)
    @PatchMapping("/{taskId}")
    public void modifyProgressStatus(@PathVariable("projectId") Long projectId,
                                     @PathVariable("taskId") Long taskId,
                                     @RequestBody ModifyProgressStatusRequestDto requestDto) {
        taskService.modifyTaskProgressStatus(projectId, taskId, requestDto);
    }

    @Operation(summary = "북마크 설정/해제", description = "북마크 설정/해제 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크 설정/해제 요청 성공")
    })
    @CheckRole(authority = TSK_READ)
    @PostMapping("/{taskId}/bookmark")
    public SetBookmarkResponseDto setBookmark(@PathVariable("projectId") Long projectId,
                                              @PathVariable("taskId") Long taskId) {
        return taskService.setBookmark(projectId, taskId);
    }

    @Operation(summary = "할 일 삭제", description = "할 일 삭제 요청입니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 삭제 성공")
    })
    @CheckRole(authority = TSK_DELETE)
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
    }

    @Operation(summary = "할 일 히스토리 조회", description = "할 일 히스토리 조회 요청입니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 히스토리 조회 성공")
    })
    @CheckRole(authority = TSK_READ)
    @GetMapping("/{taskId}/history")
    public SliceResponse<TaskHistory> taskHistory(@PathVariable Long projectId, @PathVariable Long taskId, @PageableDefault Pageable pageable) {
        return taskService.findTaskHistory(projectId, taskId, pageable);
    }
}
