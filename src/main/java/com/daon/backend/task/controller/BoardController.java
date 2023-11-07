package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.dto.request.CreateBoardRequestDto;
import com.daon.backend.task.dto.response.CreateBoardResponseDto;
import com.daon.backend.task.dto.response.FindBoardsResponseDto;
import com.daon.backend.task.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/api/workspaces/{workspaceId}/projects/{projectId}/boards")
@RestController
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "보드 생성", description = "보드 생성 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "보드 생성 성공")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CommonResponse<CreateBoardResponseDto> createBoard(@PathVariable("workspaceId") Long workspaceId,
                                                              @PathVariable("projectId") Long projectId,
                                                              @RequestBody @Valid CreateBoardRequestDto requestDto) {
        CreateBoardResponseDto result = boardService.createBoard(workspaceId, projectId, requestDto);

        return CommonResponse.createSuccess(result);
    }

    @GetMapping
    public CommonResponse<FindBoardsResponseDto> findBoards(@PathVariable("workspaceId") Long workspaceId,
                                                            @PathVariable("projectId") Long projectId) {
        FindBoardsResponseDto result = boardService.findBoards(workspaceId, projectId);

        return CommonResponse.createSuccess(result);
    }
}
