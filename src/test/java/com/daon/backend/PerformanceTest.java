package com.daon.backend;

import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.task.TaskRepository;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.TaskSummary;
import com.daon.backend.task.dto.search.SearchResponseDto;
import com.daon.backend.task.infrastructure.SearchQueryRepository;
import com.daon.backend.task.infrastructure.SecuritySessionMemberProvider;
import com.daon.backend.task.infrastructure.task.TaskJpaRepository;
import com.daon.backend.task.service.SearchService;
import com.daon.backend.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@SpringBootTest
public class PerformanceTest extends MockConfig {

    @Autowired
    TaskService taskService;

    @Autowired
    TaskJpaRepository taskJpaRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    SearchService searchService;

    @Autowired
    SearchQueryRepository searchQueryRepository;

    @MockBean
    SecuritySessionMemberProvider sessionMemberProvider;

    private String memberId = "4a22ba95-49e8-4c68-8313-54af52507e1a";

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn(memberId);
    }

    @DisplayName("전체(특정 키워드) 검색")
//    @Test
    void searchPerformanceTest() {
        long startTime = System.currentTimeMillis();
        SearchResponseDto responseDto = searchService.integratedSearchByTitle("코끼리");
        long endTime = System.currentTimeMillis();
        long diffTime = (endTime - startTime);

        System.out.println("실행 시간(ms) : " + diffTime);
        System.out.println("조회된 데이터 수 : " + responseDto.getTasks().getTotalCount());
    }

    @DisplayName("조건별 할 일 목록 조회 (본인이 담당자인 경우)")
//    @Test
    void findTasksPerformanceTest() {
        Long workspaceId = 1L;
        TaskSearchParams searchParams = new TaskSearchParams(1L, 1L, false, true);

        long startTime = System.currentTimeMillis();
        List<TaskSummary> taskSummaries = taskRepository.findTaskSummaries(memberId, workspaceId, searchParams);
//        FindTasksResponseDto responseDto = taskService.searchTasks(workspaceId, searchParams);
        long endTime = System.currentTimeMillis();
        long diffTime = (endTime - startTime);

        System.out.println("실행 시간(ms) : " + diffTime);
        System.out.println("조회된 데이터 수 : " + taskSummaries.size());
    }
}
