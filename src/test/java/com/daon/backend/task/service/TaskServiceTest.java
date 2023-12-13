package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskNotFoundException;
import com.daon.backend.task.domain.task.TaskProgressStatus;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.task.*;
import com.daon.backend.task.dto.task.history.TaskHistory;
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

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class TaskServiceTest extends MockConfig {

    @MockBean
    SessionMemberProvider sessionMemberProvider;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8");
    }

    @DisplayName("할 일 생성")
    @Test
    void createTask() {
        // given
        Long taskManagerId = 1L;
        Long projectId = 1L;
        Long boardId = 1L;
        String title = "할 일 제목";
        CreateTaskRequestDto requestDto = new CreateTaskRequestDto(
                "할 일 제목",
                null,
                taskManagerId,
                null,
                null,
                false,
                boardId
        );
        // when
        CreateTaskResponseDto responseDto = taskService.createTask(projectId, requestDto);
        Task task = taskRepository.findTaskById(responseDto.getTaskId()).orElseThrow();

        // then
        assertEquals(title, task.getTitle());
        assertEquals(taskManagerId, task.getTaskManager().getId());
        assertEquals(boardId, task.getBoard().getId());
    }

    @DisplayName("할 일 단건 조회")
    @Test
    void findTask() {
        // given
        Long taskId = 1L;

        // when
        FindTaskResponseDto responseDto = taskService.findTask(taskId);

        // then
        assertEquals(taskId, responseDto.getTaskId());
        assertEquals("Task", responseDto.getTitle());
    }

    @DisplayName("할 일 목록 조회")
    @Test
    void searchTasks() {
        // given
        Long workspaceId = 3L;
        Long projectId = 1L;
        Long boardId = 1L;
        TaskSearchParams searchParams = new TaskSearchParams(projectId, boardId, false, false);

        // when
        FindTasksResponseDto responseDto = taskService.searchTasks(workspaceId, searchParams);

        // then
        assertEquals(1, responseDto.getTasks().size());
    }

    @DisplayName("할 일 히스토리 조회")
    @Test
    void findTaskHistory() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // when
        SliceResponse<TaskHistory> taskHistory = taskService.findTaskHistory(projectId, taskId, pageable);

        // then
        assertEquals(size, taskHistory.getPageSize());
        assertEquals(0, taskHistory.getContent().size());
    }

    @DisplayName("할 일 수정")
    @Test
    void modifyTask() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;
        Long boardId = 1L;
        Long taskManagerId = 1L;
        String title = "수정된 할 일 제목";
        ModifyTaskRequestDto requestDto = new ModifyTaskRequestDto(
                title,
                null,
                null,
                null,
                true,
                null,
                boardId,
                taskManagerId
        );

        // when
        taskService.modifyTask(projectId, taskId, requestDto);
        Task task = taskRepository.findTaskById(taskId).orElseThrow();

        // then
        assertEquals(taskId, task.getId());
        assertEquals(title, task.getTitle());
        assertEquals(taskManagerId, task.getTaskManager().getId());
    }

    @DisplayName("할 일 진행 상태 변경")
    @Test
    void modifyTaskProgressStatus() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;
        ModifyProgressStatusRequestDto requestDto = new ModifyProgressStatusRequestDto(TaskProgressStatus.PROCEEDING);

        // when
        taskService.modifyTaskProgressStatus(projectId, taskId, requestDto);
        Task task = taskRepository.findTaskById(taskId).orElseThrow();

        // then
        assertEquals(taskId, task.getId());
        assertEquals(TaskProgressStatus.PROCEEDING, task.getProgressStatus());
    }

    @DisplayName("북마크 설정/해제")
    @Test
    void setBookmark() {
        // given
        Long projectId = 1L;
        Long taskId = 1L;

        // when
        SetBookmarkResponseDto responseDto1 = taskService.setBookmark(projectId, taskId);
        SetBookmarkResponseDto responseDto2 = taskService.setBookmark(projectId, taskId);

        // then
        assertTrue(responseDto1.isCreated());
        assertFalse(responseDto2.isCreated());
    }

    @DisplayName("할 일 삭제")
    @Test
    void deleteTask() {
        // given
        Long taskId = 1L;

        // when
        taskService.deleteTask(taskId);

        // then
        assertThrows(TaskNotFoundException.class, () -> taskService.findTask(taskId));
    }
}
