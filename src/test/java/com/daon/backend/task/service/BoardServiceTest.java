package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.dto.board.CreateBoardRequestDto;
import com.daon.backend.task.dto.board.FindBoardsResponseDto;
import com.daon.backend.task.dto.board.ModifyBoardRequestDto;
import com.daon.backend.task.dto.project.CreateProjectRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
public class BoardServiceTest extends MockConfig {

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
    EntityManager em;

    private Project project;

    private Long projectId;

    private Long boardId;

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

        em.flush();
        em.clear();

        project = projectRepository.findProjectById(projectId).orElseThrow();
        boardId = project.getBoards().get(1).getId();
    }

    @DisplayName("보드 생성")
    @Test
    void createBoard() {
        // when
        boardService.findBoards(projectId);

        // then
        assertEquals(2, project.getBoards().size());
    }

    @DisplayName("보드 목록 조회")
    @Test
    void findBoards() {
        // when
        FindBoardsResponseDto boards = boardService.findBoards(projectId);

        // then
        assertEquals(2, boards.getBoards().size());
    }

    @DisplayName("보드 수정")
    @Test
    void modifyBoard() {
        // given
        ModifyBoardRequestDto requestDto = new ModifyBoardRequestDto("수정된 제목");

        // when
        boardService.modifyBoard(projectId, boardId, requestDto);
        em.flush();

        // then
        assertEquals("수정된 제목", project.getBoards().get(1).getTitle());
    }

    @DisplayName("보드 삭제")
    @Test
    void deleteBoard() {
        // when
        boardService.deleteBoard(projectId, boardId);
        em.flush();
        em.clear();

        // then
        assertTrue(project.getBoards().get(1).isRemoved());
    }
}
