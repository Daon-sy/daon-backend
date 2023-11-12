package com.daon.backend.task.controller;

import com.daon.backend.common.response.CommonResponse;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.request.CreateBoardRequestDto;
import com.daon.backend.task.dto.response.FindBoardsResponseDto;
import com.daon.backend.task.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.daon.backend.task.domain.authority.Authority.BD_CREATE;
import static com.daon.backend.task.domain.authority.Authority.BD_READ;

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
    @CheckRole(authority = BD_CREATE)
    @PostMapping
    public CommonResponse<Void> createBoard(@PathVariable("workspaceId") Long workspaceId,
                                                              @PathVariable("projectId") Long projectId,
                                                              @RequestBody @Valid CreateBoardRequestDto requestDto) {
        boardService.createBoard(projectId, requestDto);

        return CommonResponse.createSuccess(null);
    }

    @Operation(summary = "보드 목록 조회", description = "보드 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "보드 목록 조회 성공")
    })
    @CheckRole(authority = BD_READ)
    @GetMapping
    public CommonResponse<FindBoardsResponseDto> findBoards(@PathVariable("workspaceId") Long workspaceId,
                                                            @PathVariable("projectId") Long projectId) {
        FindBoardsResponseDto result = boardService.findBoards(workspaceId, projectId);

        return CommonResponse.createSuccess(result);
    }
}
