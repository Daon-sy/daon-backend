package com.daon.backend.task.controller;

import com.daon.backend.common.response.ApiResponse;
import com.daon.backend.task.dto.request.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.response.CreateWorkspaceResponseDto;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ApiResponse<CreateWorkspaceResponseDto> createWorkspace(
            @RequestBody @Valid CreateWorkspaceRequestDto requestDto
    ) {
        Long workspaceId = workspaceService.createWorkspace(requestDto);
        return ApiResponse.createSuccess(new CreateWorkspaceResponseDto(workspaceId));
    }
}
