package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.dto.request.CreateBoardRequestDto;
import com.daon.backend.task.dto.response.FindBoardsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final ProjectRepository projectRepository;

    @Transactional
    public void createBoard(Long projectId, CreateBoardRequestDto requestDto) {
        Project findProject = projectRepository.findProjectWithBoardsByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        String title = requestDto.getTitle();
        findProject.throwIfTitleExist(title);
        findProject.addBoard(title);
    }

    public FindBoardsResponseDto findBoards(Long workspaceId, Long projectId) {
        Project findProject = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        List<Board> findBoards = findProject.getBoards();
        if (findBoards.isEmpty()) {
            throw new BoardNotFoundException(workspaceId, projectId);
        }

        return new FindBoardsResponseDto(
                findBoards.stream()
                        .map(FindBoardsResponseDto.BoardInfo::new)
                        .collect(Collectors.toList())
        );
    }
}
