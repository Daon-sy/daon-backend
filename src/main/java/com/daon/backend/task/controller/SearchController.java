package com.daon.backend.task.controller;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.dto.search.SearchResponseDto;
import com.daon.backend.task.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "통합 검색", description = "통합 검색 요청입니다.")
    @GetMapping("/api/search")
    public SearchResponseDto integratedSearch(@RequestParam String keyword) {
        return searchService.integratedSearchByTitle(keyword);
    }

    @Operation(summary = "워크스페이스 검색", description = "워크스페이스 검색 요청입니다.")
    @GetMapping("/api/search/workspaces")
    public PageResponse<SearchResponseDto.WorkspaceResult> workspaceSearch(
            @RequestParam String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return searchService.searchWorkspaces(keyword, pageable);
    }

    @Operation(summary = "프로젝트 검색", description = "프로젝트 검색 요청입니다.")
    @GetMapping("/api/search/projects")
    public PageResponse<SearchResponseDto.ProjectResult> projectSearch(
            @RequestParam String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return searchService.searchProjects(keyword, pageable);
    }

    @Operation(summary = "할 일 검색", description = "할 일 검색 요청입니다.")
    @GetMapping("/api/search/tasks")
    public PageResponse<SearchResponseDto.TaskResult> taskSearch(
            @RequestParam String keyword,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return searchService.searchTasks(keyword, pageable);
    }
}
