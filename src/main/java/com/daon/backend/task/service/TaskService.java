package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskBookmark;
import com.daon.backend.task.domain.task.TaskNotFoundException;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.task.*;
import com.daon.backend.task.dto.task.history.TaskHistory;
import com.daon.backend.task.dto.task.history.TaskHistoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final SessionMemberProvider sessionMemberProvider;

    /**
     * 할 일 생성
     */
    @Transactional
    public CreateTaskResponseDto createTask(Long projectId, CreateTaskRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Long taskManagerId = requestDto.getTaskManagerId();

        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        ProjectParticipant projectParticipant = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        ProjectParticipant taskManager = null;
        if (taskManagerId != null) {
            taskManager = projectParticipant;
        }

        Board board = project.getBoardByBoardId(requestDto.getBoardId());

        Task task = Task.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .emergency(requestDto.isEmergency())
                .creatorId(projectParticipant.getWorkspaceParticipant().getId())
                .taskManager(taskManager)
                .project(project)
                .board(board)
                .build();

        Long taskId = taskRepository.save(task).getId();
        return new CreateTaskResponseDto(taskId);
    }

    /**
     * 할 일 단 건 조회
     */
    public FindTaskResponseDto findTask(Long taskId) {
        return new FindTaskResponseDto(
                taskRepository.findTaskDetail(sessionMemberProvider.getMemberId(), taskId)
                        .orElseThrow(() -> new TaskNotFoundException(taskId))
        );
    }

    /**
     * 할 일 수정
     */
    @Transactional
    public void modifyTask(Long projectId, Long taskId, ModifyTaskRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Long taskManagerId = requestDto.getTaskManagerId();

        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));
        Board board = project.getBoardByBoardId(requestDto.getBoardId());
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(projectId, taskId));

        ProjectParticipant taskManager = null;
        if (taskManagerId != null) {
            taskManager = project.findProjectParticipantByProjectParticipantId(taskManagerId)
                    .orElseThrow(() -> new NotProjectParticipantException(memberId, project.getId()));
        }
        task.modifyTask(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getStartDate(),
                requestDto.getEndDate(),
                requestDto.isEmergency(),
                requestDto.getProgressStatus(),
                board,
                taskManager
        );
    }

    /**
     * 북마크 설정/해제
     */
    @Transactional
    public SetBookmarkResponseDto setBookmark(Long projectId, Long taskId) {
        String memberId = sessionMemberProvider.getMemberId();
        boolean created;

        ProjectParticipant projectParticipant =
                projectRepository.findProjectParticipantByProjectIdAndMemberId(projectId, memberId)
                        .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));
        Long projectParticipantId = projectParticipant.getId();

        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        boolean isBookmarked = taskRepository.existsTaskBookmarkByTaskIdAndProjectParticipantId(taskId, projectParticipantId);
        if (isBookmarked) {
            task.removeTaskBookmark(projectParticipant);
            created = false;
        } else {
            task.addTaskBookmark(new TaskBookmark(task, projectParticipant, memberId));
            created = true;
        }

        return new SetBookmarkResponseDto(created);
    }

    /**
     * 할 일 진행 상태 변경
     */
    @Transactional
    public void modifyTaskProgressStatus(Long projectId, Long taskId, ModifyProgressStatusRequestDto requestDto) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(projectId, taskId));
        task.modifyProgressStatus(requestDto.getProgressStatus());
    }

    /**
     * 할 일  삭제
     */
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        task.deleteTask();
    }

    public SliceResponse<TaskHistory> findTaskHistory(Long projectId, Long taskId, Pageable pageable) {
        return new SliceResponse<>(taskRepository.findTaskHistoriesByProjectIdAndTaskId(projectId, taskId, pageable));
    }
}
