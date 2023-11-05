package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.dto.request.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.response.CreateWorkspaceResponseDto;
import com.daon.backend.task.dto.response.WorkspaceListResponseDto;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResponse<CreateWorkspaceResponseDto> createWorkspace(
            @RequestBody @Valid CreateWorkspaceRequestDto requestDto
    ) {
        Long workspaceId = workspaceService.createWorkspace(requestDto);
        return CommonResponse.createSuccess(new CreateWorkspaceResponseDto(workspaceId));
    }

    @GetMapping
    public CommonResponse<WorkspaceListResponseDto> workspaceList() {
        WorkspaceListResponseDto workspaceListResponseDto = workspaceService.findAllWorkspace();
        return CommonResponse.createSuccess(workspaceListResponseDto);
    }
}
