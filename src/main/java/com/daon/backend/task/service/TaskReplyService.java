package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.domain.project.*;
import com.daon.backend.task.domain.task.*;
import com.daon.backend.task.dto.TaskReplySummary;
import com.daon.backend.task.dto.task.CreateTaskReplyRequestDto;
import com.daon.backend.task.dto.task.CreateTaskReplyResponseDto;
import com.daon.backend.task.dto.task.ModifyTaskReplyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskReplyService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final TaskReplyRepository taskReplyRepository;
    private final SessionMemberProvider sessionMemberProvider;

    /**
     * 할 일 댓글 생성
     */
    @Transactional
    public CreateTaskReplyResponseDto createTaskReply(Long projectId, Long taskId,
                                                      CreateTaskReplyRequestDto requestDto) {
        Task task = getTaskOrElseThrow(taskId);
        ProjectParticipant taskReplyWriter = getProjectParticipantOrElseThrow(projectId);

        TaskReply taskReply = TaskReply.builder()
                .content(requestDto.getContent())
                .task(task)
                .taskReplyWriter(taskReplyWriter)
                .build();

        Long taskReplyId = taskReplyRepository.save(taskReply).getId();
        return new CreateTaskReplyResponseDto(taskReplyId);
    }

    /**
     * 할 일 댓글 목록 조회
     */
    public PageResponse<TaskReplySummary> findTaskReplies(Long projectId, Long taskId, Pageable pageable) {
        ProjectParticipant taskReplyWriter = getProjectParticipantOrElseThrow(projectId);

        return new PageResponse<>(
                taskReplyRepository.findTaskReplyByTaskId(taskId, pageable)
                .map(taskReply -> new TaskReplySummary(taskReply, taskReplyWriter))
        );
    }

    /**
     * 할 일 댓글 수정
     */
    @Transactional
    public void modifyTaskReplyContent(Long projectId, Long taskId, Long taskReplyId, ModifyTaskReplyRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Task task = getTaskOrElseThrow(taskId);
        ProjectParticipant taskReplyWriter = getProjectParticipantOrElseThrow(projectId);

        if (memberId.equals(taskReplyWriter.getMemberId())) {
            task.modifyTaskReplyContent(taskReplyId, requestDto.getContent());
        }
    }

    /**
     * 할 일 댓글 삭제
     */
    @Transactional
    public void deleteTaskReply(Long projectId, Long taskId, Long taskReplyId) {
        String memberId = sessionMemberProvider.getMemberId();
        Task task = getTaskOrElseThrow(taskId);
        ProjectParticipant taskReplyWriter = getProjectParticipantOrElseThrow(projectId);

        if (memberId.equals(taskReplyWriter.getMemberId())) {
            task.deleteTaskReply(taskReplyId);
        }
    }

    public ProjectParticipant getProjectParticipantOrElseThrow(Long projectId) {
        String memberId = sessionMemberProvider.getMemberId();

        Project project = projectRepository.findProjectById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        return project.findProjectParticipantByMemberId(memberId);
    }

    public Task getTaskOrElseThrow(Long taskId) {
        return taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
    }
}
