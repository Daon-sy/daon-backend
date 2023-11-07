package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.dto.request.CreateBoardRequestDto;
import com.daon.backend.task.dto.response.CreateBoardResponseDto;
import com.daon.backend.task.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/projects/{projectId}/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public CommonResponse<CreateBoardResponseDto> createBoard(@PathVariable("workspaceId") Long workspaceId,
                                                              @PathVariable("projectId") Long projectId,
                                                              @RequestBody CreateBoardRequestDto requestDto) {
        CreateBoardResponseDto result = boardService.createBoard(workspaceId, projectId, requestDto);

        return CommonResponse.createSuccess(result);
    }
}
