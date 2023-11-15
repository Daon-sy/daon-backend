package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.task.FindTasksResponseDto;
import com.daon.backend.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/projects/tasks")
public class SearchTasksController {

    private final TaskService taskService;

    @Operation(summary = "할 일 목록 조회", description = "할 일 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 목록 조회 성공")
    })
    @CheckRole(authority = Authority.TSK_READ)
    @GetMapping
    public FindTasksResponseDto searchTasks(@ModelAttribute TaskSearchParams params) {
        return taskService.searchTasks(params);
    }
}
