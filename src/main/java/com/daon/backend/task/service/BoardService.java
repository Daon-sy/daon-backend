package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectNotFoundException;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.dto.project.CreateBoardRequestDto;
import com.daon.backend.task.dto.project.FindBoardsResponseDto;
import com.daon.backend.task.dto.project.ModifyBoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public FindBoardsResponseDto findBoards(Long projectId) {
        Project findProject = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return new FindBoardsResponseDto(
                findProject.getBoards().stream()
                        .map(FindBoardsResponseDto.BoardInfo::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void modifyBoard(Long projectId, Long boardId, ModifyBoardRequestDto requestDto) {
        Project findProject = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        findProject.modifyBoard(boardId, requestDto.getTitle());
    }
}
