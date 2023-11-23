package com.daon.backend.task.service;

import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.board.BoardNotFoundException;
import com.daon.backend.task.domain.board.BoardRepository;
import com.daon.backend.task.domain.board.CanNotDeleteBoardException;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectNotFoundException;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.dto.board.CreateBoardRequestDto;
import com.daon.backend.task.dto.board.FindBoardsResponseDto;
import com.daon.backend.task.dto.board.ModifyBoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;

    /**
     * 보드 생성
     */
    @Transactional
    public void createBoard(Long projectId, CreateBoardRequestDto requestDto) {
        Project project = projectRepository.findProjectWithBoardsByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        String title = requestDto.getTitle();
        project.throwIfTitleExist(title);
        project.addBoard(title);
    }

    /**
     * 보드 목록 조회
     */
    public FindBoardsResponseDto findBoards(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return new FindBoardsResponseDto(
                project.getBoards().stream()
                        .filter(board -> !board.isRemoved())
                        .sorted(Comparator.comparing(Board::getCreatedAt).thenComparing(Board::getId))
                        .map(FindBoardsResponseDto.BoardInfo::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 보드 수정
     */
    @Transactional
    public void modifyBoard(Long projectId, Long boardId, ModifyBoardRequestDto requestDto) {
        Board board = boardRepository.findBoardByBoardId(boardId)
                .orElseThrow(() -> new BoardNotFoundException(projectId, boardId));

        board.modifyBoard(requestDto.getTitle());
    }

    /**
     * 보드 삭제
     */
    @Transactional
    public void deleteBoard(Long projectId, Long boardId) {
        Project project = projectRepository.findProjectWithBoardsByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        if (project.getBoards().size() > 1) {
            boardRepository.deleteTasksRelatedBoard(boardId);
            boardRepository.findBoardByBoardId(boardId)
                    .orElseThrow(() -> new BoardNotFoundException(projectId, boardId))
                    .deleteBoard();
        } else {
            throw new CanNotDeleteBoardException();
        }
    }
}
