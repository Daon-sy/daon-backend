package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskReply;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.TaskReplySummary;
import com.daon.backend.task.dto.task.CreateTaskReplyRequestDto;
import com.daon.backend.task.dto.task.CreateTaskReplyResponseDto;
import com.daon.backend.task.dto.task.ModifyTaskReplyRequestDto;
import com.daon.backend.task.infrastructure.task.TaskReplyJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class TaskReplyServiceTest extends MockConfig {

    @MockBean
    SessionMemberProvider sessionMemberProvider;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskReplyService taskReplyService;

    @Autowired
    TaskReplyJpaRepository taskReplyJpaRepository;

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8");
    }

    @DisplayName("댓글 생성")
    @Test
    void createTaskReply() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;
        String replyContent = "할 일 댓글";
        CreateTaskReplyRequestDto requestDto = new CreateTaskReplyRequestDto(replyContent);

        // when
        CreateTaskReplyResponseDto responseDto = taskReplyService.createTaskReply(projectId, taskId, requestDto);
        TaskReply taskReply = taskReplyJpaRepository.findById(responseDto.getReplyId()).orElseThrow();

        // then
        assertThat(taskReply.getContent()).isEqualTo(replyContent);
    }

    @DisplayName("댓글 목록 조회")
    @Test
    void findTaskReplies() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // when
        PageResponse<TaskReplySummary> taskReplies = taskReplyService.findTaskReplies(projectId, taskId, pageable);

        // then
        assertThat(taskReplies.getContentSize()).isEqualTo(1);
        assertThat(taskReplies.getPageSize()).isEqualTo(size);
    }

    @DisplayName("댓글 수정")
    @Test
    void modifyTaskReplyContent() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;
        Long taskReplyId = 1L;
        String content = "댓글 내용 수정";
        ModifyTaskReplyRequestDto requestDto = new ModifyTaskReplyRequestDto(content);

        // when
        taskReplyService.modifyTaskReplyContent(projectId, taskId, taskReplyId, requestDto);
        TaskReply taskReply = taskReplyJpaRepository.findById(taskReplyId).orElseThrow();

        // then
        assertThat(taskReply.getId()).isEqualTo(taskReplyId);
        assertThat(taskReply.getContent()).isEqualTo(content);
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteTaskReply() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;
        Long taskReplyId = 1L;

        // when
        taskReplyService.deleteTaskReply(projectId, taskId, taskReplyId);
        Task task = taskRepository.findTaskById(taskId).orElseThrow();

        // then
        assertThat(task.getTaskReplies().size()).isEqualTo(0);
    }
}
