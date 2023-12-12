package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.board.CreateBoardRequestDto;
import com.daon.backend.task.dto.project.CreateProjectRequestDto;
import com.daon.backend.task.dto.search.SearchResponseDto;
import com.daon.backend.task.dto.task.CreateTaskRequestDto;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class SearchServiceTest extends MockConfig {

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
    SearchService searchService;

    @Autowired
    EntityManager em;

    private Long workspaceId;

    private Long projectId;

    private Long taskId;

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

        em.flush();
        em.clear();
    }

    @DisplayName("통합 검색")
    @Test
    void integratedSearchByTitle() {
        // when
        String keyword = "제목";
        SearchResponseDto responseDto = searchService.integratedSearchByTitle(keyword);

        // then
        assertEquals(1, responseDto.getWorkspaces().getContent().size());
        assertEquals(workspaceId, responseDto.getWorkspaces().getContent().get(0).getWorkspaceId());
        assertEquals(1, responseDto.getProjects().getContent().size());
        assertEquals(projectId, responseDto.getProjects().getContent().get(0).getProjectId());
        assertEquals(1, responseDto.getTasks().getContent().size());
        assertEquals(taskId, responseDto.getTasks().getContent().get(0).getTaskId());
    }

    @DisplayName("워크스페이스 검색")
    @Test
    void searchWorkspaces() {
        // given
        String keyword = "제목";
        Pageable pageable = getPageable();

        // when
        PageResponse<SearchResponseDto.WorkspaceResult> responseDto = searchService.searchWorkspaces(keyword, pageable);

        // then
        assertEquals(1, responseDto.getContent().size());
        assertEquals(workspaceId, responseDto.getContent().get(0).getWorkspaceId());
    }

    @DisplayName("프로젝트 검색")
    @Test
    void searchProjects() {
        // given
        String keyword = "제목";
        Pageable pageable = getPageable();

        // when
        PageResponse<SearchResponseDto.ProjectResult> responseDto = searchService.searchProjects(keyword, pageable);

        // then
        assertEquals(1, responseDto.getContent().size());
        assertEquals(projectId, responseDto.getContent().get(0).getProjectId());
    }

    @DisplayName("할 일 검색")
    @Test
    void searchTasks() {
        // given
        String keyword = "제목";
        Pageable pageable = getPageable();

        // when
        PageResponse<SearchResponseDto.TaskResult> responseDto = searchService.searchTasks(keyword, pageable);

        // then
        assertEquals(1, responseDto.getContent().size());
        assertEquals(taskId, responseDto.getContent().get(0).getTaskId());
    }

    private Pageable getPageable() {
        int page = 0;
        int size = 10;
        return PageRequest.of(page, size);
    }
}
