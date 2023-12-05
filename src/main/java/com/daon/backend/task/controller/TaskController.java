package com.daon.backend.task.controller;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.task.*;
import com.daon.backend.task.dto.task.history.TaskHistory;
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
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "할 일 생성", description = "할 일 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "할 일 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = TSK_CREATE)
    @PostMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks")
    public CreateTaskResponseDto createTask(@PathVariable("projectId") Long projectId,
                                            @RequestBody @Valid CreateTaskRequestDto requestDto) {
        return taskService.createTask(projectId, requestDto);
    }

    @Operation(summary = "할 일 수정", description = "할 일 수정 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 수정 성공")
    })
    @CheckRole(authority = TSK_UPDATE)
    @PutMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}")
    public void modifyTask(@PathVariable Long projectId,
                           @PathVariable Long taskId,
                           @RequestBody @Valid ModifyTaskRequestDto requestDto) {
        taskService.modifyTask(projectId, taskId, requestDto);
    }

    @Operation(summary = "할 일 단건 조회", description = "할 일 단건 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 단건 조회 성공")
    })
    @CheckRole(authority = TSK_READ)
    @GetMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}")
    public FindTaskResponseDto findTask(@PathVariable Long taskId) {
        return taskService.findTask(taskId);
    }

    @Operation(summary = "할 일 목록 조회", description = "할 일 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 목록 조회 성공")
    })
    @CheckRole(authority = Authority.TSK_READ)
    @GetMapping("/api/workspaces/{workspaceId}/projects/tasks")
    public FindTasksResponseDto searchTasks(@PathVariable Long workspaceId, @ModelAttribute TaskSearchParams params) {
        return taskService.searchTasks(workspaceId, params);
    }

    @Operation(summary = "할 일 진행 상태 변경", description = "할 일 진행 상태 변경 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 진행 상태 변경 성공")
    })
    @CheckRole(authority = TSK_UPDATE)
    @PatchMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}")
    public void modifyProgressStatus(@PathVariable Long projectId,
                                     @PathVariable Long taskId,
                                     @RequestBody ModifyProgressStatusRequestDto requestDto) {
        taskService.modifyTaskProgressStatus(projectId, taskId, requestDto);
    }

    @Operation(summary = "북마크 설정/해제", description = "북마크 설정/해제 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "북마크 설정/해제 요청 성공")
    })
    @CheckRole(authority = TSK_READ)
    @PostMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}/bookmark")
    public SetBookmarkResponseDto setBookmark(@PathVariable Long projectId,
                                              @PathVariable Long taskId) {
        return taskService.setBookmark(projectId, taskId);
    }

    @Operation(summary = "할 일 삭제", description = "할 일 삭제 요청입니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 삭제 성공")
    })
    @CheckRole(authority = TSK_DELETE)
    @DeleteMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }

    @Operation(summary = "할 일 히스토리 조회", description = "할 일 히스토리 조회 요청입니다..")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 히스토리 조회 성공")
    })
    @CheckRole(authority = TSK_READ)
    @GetMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks/{taskId}/history")
    public SliceResponse<TaskHistory> taskHistory(@PathVariable Long projectId,
                                                  @PathVariable Long taskId,
                                                  @PageableDefault Pageable pageable) {
        return taskService.findTaskHistory(projectId, taskId, pageable);
    }
}
