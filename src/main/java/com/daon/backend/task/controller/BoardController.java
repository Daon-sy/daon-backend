package com.daon.backend.task.controller;

import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.board.CreateBoardRequestDto;
import com.daon.backend.task.dto.board.FindBoardsResponseDto;
import com.daon.backend.task.dto.board.ModifyBoardRequestDto;
import com.daon.backend.task.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.daon.backend.task.domain.authority.Authority.*;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "보드 생성", description = "보드 생성 요청입니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @CheckRole(authority = BD_CREATE)
    @PostMapping("/api/workspaces/{workspaceId}/projects/{projectId}/boards")
    public void createBoard(@PathVariable Long projectId,
                            @RequestBody @Valid CreateBoardRequestDto requestDto) {
        boardService.createBoard(projectId, requestDto);
    }

    @Operation(summary = "보드 목록 조회", description = "보드 목록 조회 요청입니다.")
    @CheckRole(authority = BD_READ)
    @GetMapping("/api/workspaces/{workspaceId}/projects/{projectId}/boards")
    public FindBoardsResponseDto findBoards(@PathVariable Long projectId) {
        return boardService.findBoards(projectId);
    }

    @Operation(summary = "보드 수정", description = "보드 수정 요청입니다.")
    @CheckRole(authority = BD_UPDATE)
    @PutMapping("/api/workspaces/{workspaceId}/projects/{projectId}/boards/{boardId}")
    public void modifyBoard(@PathVariable Long projectId,
                            @PathVariable Long boardId,
                            @RequestBody @Valid ModifyBoardRequestDto requestDto) {
        boardService.modifyBoard(projectId, boardId, requestDto);
    }

    @Operation(summary = "보드 삭제", description = "보드 삭제 요청입니다.")
    @CheckRole(authority = BD_DELETE)
    @DeleteMapping("/api/workspaces/{workspaceId}/projects/{projectId}/boards/{boardId}")
    public void deleteBoard(@PathVariable Long projectId,
                            @PathVariable Long boardId) {
        boardService.deleteBoard(projectId, boardId);
    }
}
