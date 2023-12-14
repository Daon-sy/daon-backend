package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.config.EmailConfig;
import com.daon.backend.config.S3Config;
import com.daon.backend.image.infrastructure.S3ImageFileService;
import com.daon.backend.mail.service.MailService;
import com.daon.backend.task.dto.search.SearchResponseDto;
import com.daon.backend.task.infrastructure.SearchQueryRepository;
import com.daon.backend.task.infrastructure.SecuritySessionMemberProvider;
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
public class SearchServiceTest {

    @MockBean
    S3Config s3Config;

    @MockBean
    EmailConfig emailConfig;

    @MockBean
    S3ImageFileService s3ImageFileService;

    @MockBean
    MailService mailService;

    @MockBean
    SecuritySessionMemberProvider sessionMemberProvider;

    @Autowired
    SearchQueryRepository searchQueryRepository;

    @Autowired
    SearchService searchService;

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8");
    }

    @DisplayName("통합 검색")
    @Test
    void integratedSearchByTitle() {
        // given
        Long projectId = 1L;

        // when
        String keyword = "project";
        SearchResponseDto responseDto = searchService.integratedSearchByTitle(keyword);

        // then
        assertThat(responseDto.getWorkspaces().getContentSize()).isEqualTo(0);
        assertThat(responseDto.getProjects().getContentSize()).isEqualTo(1);
        assertThat(responseDto.getProjects().getContent().get(0).getProjectId()).isEqualTo(projectId);
        assertThat(responseDto.getTasks().getContentSize()).isEqualTo(0);
    }

    @DisplayName("워크스페이스 검색")
    @Test
    void searchWorkspaces() {
        // given
        Long workspaceId = 1L;
        String keyword = "USER";
        Pageable pageable = getPageable();

        // when
        PageResponse<SearchResponseDto.WorkspaceResult> responseDto = searchService.searchWorkspaces(keyword, pageable);

        // then
        assertThat(responseDto.getContentSize()).isEqualTo(1);
        assertThat(responseDto.getContent().get(0).getWorkspaceId()).isEqualTo(workspaceId);
    }

    @DisplayName("프로젝트 검색")
    @Test
    void searchProjects() {
        // given
        Long projectId = 1L;
        String keyword = "project";
        Pageable pageable = getPageable();

        // when
        PageResponse<SearchResponseDto.ProjectResult> responseDto = searchService.searchProjects(keyword, pageable);

        // then
        assertThat(responseDto.getContentSize()).isEqualTo(1);
        assertThat(responseDto.getContent().get(0).getProjectId()).isEqualTo(projectId);
    }

    @DisplayName("할 일 검색")
    @Test
    void searchTasks() {
        // given
        Long taskId = 1L;
        String keyword = "Task";
        Pageable pageable = getPageable();

        // when
        PageResponse<SearchResponseDto.TaskResult> responseDto = searchService.searchTasks(keyword, pageable);

        // then
        assertThat(responseDto.getContentSize()).isEqualTo(1);
        assertThat(responseDto.getContent().get(0).getTaskId()).isEqualTo(taskId);
    }

    private Pageable getPageable() {
        int page = 0;
        int size = 10;
        return PageRequest.of(page, size);
    }
}
