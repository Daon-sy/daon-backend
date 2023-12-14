package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.task.FindTasksResponseDto;
import com.daon.backend.task.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TaskListController {

    private final TaskService taskService;

    @Operation(summary = "할 일 목록 조회", description = "할 일 목록 조회 요청입니다.")
    @CheckRole(authority = Authority.TSK_READ)
    @GetMapping("/api/workspaces/{workspaceId}/projects/boards/tasks")
    public FindTasksResponseDto searchTasks(@PathVariable Long workspaceId, @ModelAttribute TaskSearchParams params) {
        return taskService.searchTasks(workspaceId, params);
    }
}
