package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceNotice;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeResponseDto;
import com.daon.backend.task.dto.workspace.FindWorkspaceNoticeResponseDto;
import com.daon.backend.task.dto.workspace.ModifyWorkspaceNoticeRequestDto;
import com.daon.backend.task.infrastructure.workspace.WorkspaceNoticeJpaRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
public class WorkspaceNoticeServiceTest extends MockConfig {

    @MockBean
    SessionMemberProvider sessionMemberProvider;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    WorkspaceNoticeService workspaceNoticeService;

    @Autowired
    WorkspaceNoticeJpaRepository workspaceNoticeJpaRepository;

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8");
    }

    @DisplayName("공지사항 생성")
    @Test
    void createWorkspaceNotice() {
        // given
        Long workspaceId = 3L;
        String noticeTitle = "공지사항 제목";
        String noticeContent = "공지사항 내용";
        CreateWorkspaceNoticeRequestDto requestDto = new CreateWorkspaceNoticeRequestDto(noticeTitle, noticeContent);

        // when
        CreateWorkspaceNoticeResponseDto responseDto =
                workspaceNoticeService.createWorkspaceNotice(workspaceId, requestDto);

        // then
        WorkspaceNotice notice = workspaceNoticeJpaRepository.findById(responseDto.getNoticeId()).orElseThrow();
        assertEquals(noticeTitle, notice.getTitle());
        assertEquals(noticeContent, notice.getContent());
    }

    @DisplayName("공지사항 단건 조회")
    @Test
    void findWorkspaceNotice() {
        // given
        Long workspaceNoticeId = 1L;

        // when
        FindWorkspaceNoticeResponseDto responseDto = workspaceNoticeService.findWorkspaceNotice(workspaceNoticeId);

        // then
        assertEquals(workspaceNoticeId, responseDto.getNoticeId());
        assertEquals("notice title", responseDto.getTitle());
        assertEquals("notice content", responseDto.getContent());
    }

    @DisplayName("공지사항 목록 조회")
    @Test
    void findWorkspaceNotices() {
        // given
        Long workspaceId = 3L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // when
        PageResponse<WorkspaceNoticeSummary> responseDto =
                workspaceNoticeService.findWorkspaceNotices(workspaceId, null, pageable);

        // then
        assertEquals(1, responseDto.getContent().size());
        assertEquals(size, responseDto.getPageSize());
    }

    @DisplayName("공지사항 수정")
    @Test
    void modifyWorkspaceNoticeContent() {
        // given
        Long workspaceId = 3L;
        Long workspaceNoticeId = 1L;
        String editTitle = "수정된 공지사항 제목";
        String editContent = "수정된 공지사항 내용";
        ModifyWorkspaceNoticeRequestDto requestDto = new ModifyWorkspaceNoticeRequestDto(editTitle, editContent);

        // when
        workspaceNoticeService.modifyWorkspaceNoticeContent(workspaceId, workspaceNoticeId, requestDto);
        WorkspaceNotice workspaceNotice = workspaceNoticeJpaRepository.findById(workspaceNoticeId).orElseThrow();

        // then
        assertEquals(workspaceNoticeId, workspaceNotice.getId());
        assertEquals(editTitle, workspaceNotice.getTitle());
        assertEquals(editContent, workspaceNotice.getContent());
    }

    @DisplayName("공지사항 삭제")
    @Test
    void deleteWorkspaceNotice() {
        // given
        Long workspaceId = 3L;
        Long workspaceNoticeId = 1L;

        // when
        workspaceNoticeService.deleteWorkspaceNotice(workspaceId, workspaceNoticeId);
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();

        // then
        assertEquals(0, workspace.getWorkspaceNotices().size());
    }
}
