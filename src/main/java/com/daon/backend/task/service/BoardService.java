package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.project.CreateBoardRequestDto;
import com.daon.backend.task.dto.project.FindBoardsResponseDto;
import com.daon.backend.task.dto.project.ModifyBoardRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

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
                        .sorted(Comparator.comparing(Board::getCreatedAt).thenComparing(Board::getId))
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

    @Transactional
    public void deleteBoard(Long projectId, Long boardId) {
        Project findProject = projectRepository.findProjectByProjectId(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        Board board = findProject.findBoardByBoardId(boardId)
                .orElseThrow(() -> new BoardNotFoundException(projectId, boardId));
        List<Task> tasks = taskRepository.findTasksByBoard(board);
        tasks.forEach(Task::removeBoard);

        findProject.deleteBoard(boardId);
    }
}
