package com.daon.backend.task.service;

import com.daon.backend.task.domain.project.NotProjectParticipantException;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.task.*;
import com.daon.backend.task.dto.ReplySummary;
import com.daon.backend.task.dto.task.CreateReplyRequestDto;
import com.daon.backend.task.dto.task.CreateReplyResponseDto;
import com.daon.backend.task.dto.task.FindRepliesResponseDto;
import com.daon.backend.task.dto.task.ModifyReplyRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {

    private final TaskRepository taskRepository;
    private final ReplyRepository replyRepository;
    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public CreateReplyResponseDto createReply(Long projectId,
                                              Long taskId,
                                              CreateReplyRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant writer = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        Reply reply = Reply.builder()
                .content(requestDto.getContent())
                .task(task)
                .writer(writer)
                .build();

        Long replyId = replyRepository.save(reply).getId();
        return new CreateReplyResponseDto(replyId);
    }

    public FindRepliesResponseDto findReplies(Long projectId,
                                              Long taskId) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant currentParticipant = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));
        List<ReplySummary> list = replyRepository.findReplyListByTaskId(taskId).stream()
                .map(reply -> new ReplySummary(reply, currentParticipant))
                .collect(Collectors.toList());

        return new FindRepliesResponseDto(list, taskId);
    }

    @Transactional
    public void modifyReply(Long projectId, Long taskId, Long replyId, ModifyReplyRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant writer = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        if (memberId.equals(writer.getMemberId())) {
            task.modifyContent(replyId, requestDto.getContent());
        }
    }

    @Transactional
    public void deleteReply(Long projectId, Long taskId, Long replyId) {
        String memberId = sessionMemberProvider.getMemberId();

        Task task = taskRepository.findTaskByTaskId(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        Project project = task.getProject();
        ProjectParticipant writer = project.findProjectParticipantByMemberId(memberId)
                .orElseThrow(() -> new NotProjectParticipantException(memberId, projectId));

        if (memberId.equals(writer.getMemberId())) {
            task.deleteReply(replyId);
        }
    }
}
