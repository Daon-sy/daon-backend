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

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(notice.getTitle()).isEqualTo(noticeTitle);
        assertThat(notice.getContent()).isEqualTo(noticeContent);
    }

    @DisplayName("공지사항 단건 조회")
    @Test
    void findWorkspaceNotice() {
        // given
        Long workspaceNoticeId = 1L;

        // when
        FindWorkspaceNoticeResponseDto responseDto = workspaceNoticeService.findWorkspaceNotice(workspaceNoticeId);

        // then
        assertThat(responseDto.getNoticeId()).isEqualTo(workspaceNoticeId);
        assertThat(responseDto.getTitle()).isEqualTo("notice title");
        assertThat(responseDto.getContent()).isEqualTo("notice content");
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
        assertThat(responseDto.getContentSize()).isEqualTo(1);
        assertThat(responseDto.getPageSize()).isEqualTo(size);
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
        assertThat(workspaceNotice.getId()).isEqualTo(workspaceNoticeId);
        assertThat(workspaceNotice.getTitle()).isEqualTo(editTitle);
        assertThat(workspaceNotice.getContent()).isEqualTo(editContent);
    }

    @DisplayName("공지사항 삭제")
    @Test
    void deleteWorkspaceNotice() {
        // given
        Long workspaceId = 3L;
        Long workspaceNoticeId = 1L;

        // when
        workspaceNoticeService.deleteWorkspaceNotice(workspaceId, workspaceNoticeId);
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();

        // then
        assertThat(workspace.getWorkspaceNotices().size()).isEqualTo(0);
    }
}
