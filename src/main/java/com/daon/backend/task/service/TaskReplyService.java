package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.NotProjectParticipantException;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.task.*;
import com.daon.backend.task.dto.TaskReplySummary;
import com.daon.backend.task.dto.task.CreateTaskReplyRequestDto;
import com.daon.backend.task.dto.task.CreateTaskReplyResponseDto;
import com.daon.backend.task.dto.task.FindTaskRepliesResponseDto;
import com.daon.backend.task.dto.task.ModifyTaskReplyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskReplyService {

    private final TaskRepository taskRepository;
    private final TaskReplyRepository taskReplyRepository;
    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public CreateTaskReplyResponseDto createTaskReply(Long projectId,
                                                      Long taskId,
                                                      CreateTaskReplyRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant taskReplyWriter = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        TaskReply taskReply = TaskReply.builder()
                .content(requestDto.getContent())
                .task(task)
                .taskReplyWriter(taskReplyWriter)
                .build();

        Long taskReplyId = taskReplyRepository.save(taskReply).getId();
        return new CreateTaskReplyResponseDto(taskReplyId);
    }

    public FindTaskRepliesResponseDto findTaskReplies(Long projectId,
                                                      Long taskId) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant currentParticipant = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));
        List<TaskReplySummary> list = taskReplyRepository.findTaskReplyByTaskId(taskId).stream()
                .map(taskReply -> new TaskReplySummary(taskReply, currentParticipant))
                .collect(Collectors.toList());

        return new FindTaskRepliesResponseDto(list, taskId);
    }

    @Transactional
    public void modifyTaskReplyContent(Long projectId, Long taskId, Long taskReplyId, ModifyTaskReplyRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant taskReplyWriter = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        if (memberId.equals(taskReplyWriter.getMemberId())) {
            task.modifyTaskReplyContent(taskReplyId, requestDto.getContent());
        }
    }

    @Transactional
    public void deleteTaskReply(Long projectId, Long taskId, Long taskReplyId) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant taskReplyWriter = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        if (memberId.equals(taskReplyWriter.getMemberId())) {
            task.deleteTaskReply(taskReplyId);
        }
    }
}
