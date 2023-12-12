package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskNotFoundException;
import com.daon.backend.task.domain.task.TaskProgressStatus;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.board.CreateBoardRequestDto;
import com.daon.backend.task.dto.project.CreateProjectRequestDto;
import com.daon.backend.task.dto.task.*;
import com.daon.backend.task.dto.task.history.TaskHistory;
import com.daon.backend.task.dto.workspace.CreateWorkspaceRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
public class TaskServiceTest extends MockConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BoardService boardService;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    TaskService taskService;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    EntityManager em;

    private Long workspaceId;

    private Long projectId;

    private Long boardId;

    private Long taskId;

    private Long taskManagerId;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .username("user")
                .password("1234")
                .name("유저")
                .email("user@email.com")
                .passwordEncoder(passwordEncoder)
                .build();

        String savedMemberId = memberRepository.save(member).getId();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new MemberPrincipal(savedMemberId),
                        null
                )
        );

        CreateWorkspaceRequestDto createWorkspaceRequestDto = new CreateWorkspaceRequestDto(
                new CreateWorkspaceRequestDto.WorkspaceInfo(
                        "워크스페이스 제목",
                        null,
                        "워크스페이스 설명",
                        "워크스페이스 주제"
                ),
                new CreateWorkspaceRequestDto.WorkspaceProfileInfo(
                        "홍길동",
                        null,
                        "user@email.com"
                )
        );

        workspaceId = workspaceService.createWorkspace(createWorkspaceRequestDto).getWorkspaceId();

        CreateProjectRequestDto createProjectRequestDto =
                new CreateProjectRequestDto("프로젝트 제목", "프로젝트 설명");
        projectId = projectService.createProject(workspaceId, createProjectRequestDto).getProjectId();

        CreateBoardRequestDto createBoardRequestDto = new CreateBoardRequestDto("보드 제목");
        boardService.createBoard(projectId, createBoardRequestDto);

        Project project = projectRepository.findProjectById(projectId).orElseThrow();
        boardId = project.getBoards().get(1).getId();

        taskManagerId = project.findProjectParticipantByMemberId(savedMemberId).orElseThrow().getId();
        CreateTaskRequestDto createTaskRequestDto = new CreateTaskRequestDto(
                "할 일 제목",
                null,
                taskManagerId,
                null,
                null,
                false,
                boardId
        );
        taskId = taskService.createTask(projectId, createTaskRequestDto).getTaskId();

        em.flush();
        em.clear();
    }

    @DisplayName("할 일 생성")
    @Test
    void createTask() {
        // when
        Task task = taskRepository.findTaskById(taskId).orElseThrow();

        // then
        assertEquals(taskId, task.getId());
        assertEquals("할 일 제목", task.getTitle());
    }

    @DisplayName("할 일 단건 조회")
    @Test
    void findTask() {
        // when
        FindTaskResponseDto responseDto = taskService.findTask(taskId);

        // then
        assertEquals(taskId, responseDto.getTaskId());
        assertEquals("할 일 제목", responseDto.getTitle());
    }

    @DisplayName("할 일 목록 조회")
    @Test
    void searchTasks() {
        // given
        TaskSearchParams searchParams = new TaskSearchParams(projectId, boardId, false, true);

        // when
        FindTasksResponseDto responseDto = taskService.searchTasks(workspaceId, searchParams);

        // then
        assertEquals(1, responseDto.getTasks().size());
        assertEquals(taskId, responseDto.getTasks().get(0).getTaskId());
    }

    @DisplayName("할 일 히스토리 조회")
    @Test
    void findTaskHistory() {
        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // when
        SliceResponse<TaskHistory> taskHistory = taskService.findTaskHistory(projectId, taskId, pageable);

        // then
        assertEquals(size, taskHistory.getPageSize());
    }

    @DisplayName("할 일 수정")
    @Test
    void modifyTask() {
        // given
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
        // when
        taskService.deleteTask(taskId);

        // then
        assertThrows(TaskNotFoundException.class, () -> taskService.findTask(taskId));
    }
}
