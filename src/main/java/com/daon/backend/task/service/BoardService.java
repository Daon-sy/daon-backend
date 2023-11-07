package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.Board;
import com.daon.backend.task.domain.project.BoardRepository;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectRepository;
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

        Board board = new Board(findProject, requestDto.getTitle());
        Board savedBoard = boardRepository.save(board);

        return new CreateBoardResponseDto(savedBoard.getId());
    }
}
