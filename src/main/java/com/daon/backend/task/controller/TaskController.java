package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.request.CreateTaskRequestDto;
import com.daon.backend.task.dto.response.CreateTaskResponseDto;
import com.daon.backend.task.dto.response.TaskListResponseDto;
import com.daon.backend.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.daon.backend.task.domain.authority.Authority.TSK_CREATE;
import static com.daon.backend.task.domain.authority.Authority.TSK_READ;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Task", description = "Task domain API")
@RequestMapping("/api/workspaces/{workspaceId}/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "할일 생성", description = "할일 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "할일 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = TSK_CREATE)
    @PostMapping
    public CommonResponse<CreateTaskResponseDto> createTask(@PathVariable Long projectId,
                                                            @RequestBody CreateTaskRequestDto requestDto) {
        CreateTaskResponseDto result = taskService.createTask(projectId, requestDto);

        return CommonResponse.createSuccess(result);
    }

    @Operation(summary = "할일 목록 조회", description = "할일 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할일 목록 조회 성공")
    })
    @CheckRole(authority = TSK_READ)
    @GetMapping
    public CommonResponse<TaskListResponseDto> findTasks(@PathVariable Long projectId) {
        TaskListResponseDto result = taskService.findAllTaskInProject(projectId);
        return CommonResponse.createSuccess(result);
    }
}
