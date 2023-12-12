package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.workspace.FindWorkspaceNoticeResponseDto;
import com.daon.backend.task.dto.workspace.ModifyWorkspaceNoticeRequestDto;
import com.daon.backend.task.infrastructure.workspace.WorkspaceNoticeJpaRepository;
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
public class WorkspaceNoticeServiceTest extends MockConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    WorkspaceNoticeService workspaceNoticeService;

    @Autowired
    WorkspaceNoticeJpaRepository workspaceNoticeJpaRepository;

    @Autowired
    EntityManager em;

    private Long workspaceId;

    private Long workspaceNoticeId;

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

        CreateWorkspaceRequestDto requestDto = new CreateWorkspaceRequestDto(
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

        workspaceId = workspaceService.createWorkspace(requestDto).getWorkspaceId();

        CreateWorkspaceNoticeRequestDto createWorkspaceNoticeRequestDto = new CreateWorkspaceNoticeRequestDto(
                "공지사항 제목",
                "공지사항 내용"
        );
        workspaceNoticeId = workspaceNoticeService.createWorkspaceNotice(workspaceId, createWorkspaceNoticeRequestDto).getNoticeId();

        em.flush();
        em.clear();
    }

    @DisplayName("공지사항 생성")
    @Test
    void createWorkspaceNotice() {
        // when
        WorkspaceNotice workspaceNotice = workspaceNoticeJpaRepository.findById(workspaceNoticeId).orElseThrow();

        // then
        assertEquals(workspaceNoticeId, workspaceNotice.getId());
        assertEquals("공지사항 제목", workspaceNotice.getTitle());
        assertEquals("공지사항 내용", workspaceNotice.getContent());
    }

    @DisplayName("공지사항 단건 조회")
    @Test
    void findWorkspaceNotice() {
        // when
        FindWorkspaceNoticeResponseDto responseDto = workspaceNoticeService.findWorkspaceNotice(workspaceNoticeId);

        // then
        assertEquals(workspaceNoticeId, responseDto.getNoticeId());
        assertEquals("공지사항 제목", responseDto.getTitle());
        assertEquals("공지사항 내용", responseDto.getContent());
    }

    @DisplayName("공지사항 목록 조회")
    @Test
    void findWorkspaceNotices() {
        // given
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // when
        PageResponse<WorkspaceNoticeSummary> responseDto =
                workspaceNoticeService.findWorkspaceNotices(workspaceId, null, pageable);

        // then
        assertEquals(size, responseDto.getPageSize());
        assertEquals(1, responseDto.getContent().size());
    }

    @DisplayName("공지사항 수정")
    @Test
    void modifyWorkspaceNoticeContent() {
        // given
        String title = "수정된 공지사항 제목";
        String content = "수정된 공지사항 내용";
        ModifyWorkspaceNoticeRequestDto requestDto = new ModifyWorkspaceNoticeRequestDto(title, content);

        // when
        workspaceNoticeService.modifyWorkspaceNoticeContent(workspaceId, workspaceNoticeId, requestDto);
        WorkspaceNotice workspaceNotice = workspaceNoticeJpaRepository.findById(workspaceNoticeId).orElseThrow();

        // then
        assertEquals(workspaceNoticeId, workspaceNotice.getId());
        assertEquals(title, workspaceNotice.getTitle());
        assertEquals(content, workspaceNotice.getContent());
    }

    @DisplayName("공지사항 삭제")
    @Test
    void deleteWorkspaceNotice() {
        // when
        workspaceNoticeService.deleteWorkspaceNotice(workspaceId, workspaceNoticeId);
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();

        // then
        assertEquals(0, workspace.getWorkspaceNotices().size());
    }
}
