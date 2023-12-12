package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskReply;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.TaskReplySummary;
import com.daon.backend.task.dto.board.CreateBoardRequestDto;
import com.daon.backend.task.dto.project.CreateProjectRequestDto;
import com.daon.backend.task.dto.task.CreateTaskReplyRequestDto;
import com.daon.backend.task.dto.task.CreateTaskRequestDto;
import com.daon.backend.task.dto.task.ModifyTaskReplyRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceRequestDto;
import com.daon.backend.task.infrastructure.task.TaskReplyJpaRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class TaskReplyServiceTest extends MockConfig {

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
    TaskReplyService taskReplyService;

    @Autowired
    TaskReplyJpaRepository taskReplyJpaRepository;

    @Autowired
    EntityManager em;

    private Long projectId;

    private Long taskId;

    private Long taskReplyId;

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

        Long workspaceId = workspaceService.createWorkspace(createWorkspaceRequestDto).getWorkspaceId();

        CreateProjectRequestDto createProjectRequestDto =
                new CreateProjectRequestDto("프로젝트 제목", "프로젝트 설명");
        projectId = projectService.createProject(workspaceId, createProjectRequestDto).getProjectId();

        CreateBoardRequestDto createBoardRequestDto = new CreateBoardRequestDto("보드 제목");
        boardService.createBoard(projectId, createBoardRequestDto);

        Project project = projectRepository.findProjectById(projectId).orElseThrow();
        Long boardId = project.getBoards().get(1).getId();

        Long taskManagerId = project.findProjectParticipantByMemberId(savedMemberId).orElseThrow().getId();
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

        CreateTaskReplyRequestDto createTaskReplyRequestDto = new CreateTaskReplyRequestDto("할 일 댓글");
        taskReplyId = taskReplyService.createTaskReply(projectId, taskId, createTaskReplyRequestDto).getReplyId();

        em.flush();
        em.clear();
    }

    @DisplayName("댓글 생성")
    @Test
    void createTaskReply() {
        // when
        TaskReply taskReply = taskReplyJpaRepository.findById(taskReplyId).orElseThrow();

        // then
        assertEquals(taskReplyId, taskReply.getId());
        assertEquals("할 일 댓글", taskReply.getContent());
    }

    @DisplayName("댓글 목록 조회")
    @Test
    void findTaskReplies() {
        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // when
        PageResponse<TaskReplySummary> taskReplies = taskReplyService.findTaskReplies(projectId, taskId, pageable);

        // then
        assertEquals(1, taskReplies.getContentSize());
        assertEquals(size, taskReplies.getPageSize());
    }

    @DisplayName("댓글 수정")
    @Test
    void modifyTaskReplyContent() {
        // given
        String content = "댓글 내용 수정";
        ModifyTaskReplyRequestDto requestDto = new ModifyTaskReplyRequestDto(content);

        // when
        taskReplyService.modifyTaskReplyContent(projectId, taskId, taskReplyId, requestDto);
        TaskReply taskReply = taskReplyJpaRepository.findById(taskReplyId).orElseThrow();

        // then
        assertEquals(taskReplyId, taskReply.getId());
        assertEquals(content, taskReply.getContent());
    }

    @DisplayName("댓글 삭제")
    @Test
    void deleteTaskReply() {
        // when
        taskReplyService.deleteTaskReply(projectId, taskId, taskReplyId);
        Task task = taskRepository.findTaskById(taskId).orElseThrow();

        // then
        assertEquals(0, task.getTaskReplies().size());
    }
}
