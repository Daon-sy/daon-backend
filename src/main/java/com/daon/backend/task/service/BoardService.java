package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.workspace.ProjectNotFoundException;
import com.daon.backend.task.dto.request.CreateBoardRequestDto;
import com.daon.backend.task.dto.response.CreateBoardResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public CreateBoardResponseDto createBoard(Long workspaceId, Long projectId, CreateBoardRequestDto requestDto) {
        Project findProject = projectRepository.findProjectByIdAndWorkspaceId(projectId, workspaceId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        String title = requestDto.getTitle();
        if (boardRepository.existsBoardByTitle(title)) {
            throw new SameBoardExistsException(title);
        }

        Board board = new Board(findProject, title);
        Board savedBoard = boardRepository.save(board);

        return new CreateBoardResponseDto(savedBoard.getId());
    }
}
