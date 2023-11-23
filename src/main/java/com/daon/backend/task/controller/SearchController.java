package com.daon.backend.task.controller;

import com.daon.backend.common.response.slice.SliceResponse;
import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.dto.TaskSearchParams;
import com.daon.backend.task.dto.task.FindTasksResponseDto;
import com.daon.backend.task.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "할 일 목록 조회", description = "할 일 목록 조회 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할 일 목록 조회 성공")
    })
    @CheckRole(authority = Authority.TSK_READ)
    @GetMapping("/workspaces/{workspaceId}/projects/tasks")
    public FindTasksResponseDto searchTasks(@ModelAttribute TaskSearchParams params) {
        return searchService.searchTasks(params);
    }

    @Operation(summary = "통합 검색", description = "통합 검색 요청입니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "통합 검색 성공")
    })
    @GetMapping("/search")
    public <T> SliceResponse<T> integratedSearch(@RequestParam("target") String target,
                                                @RequestParam("title") String title,
                                                Pageable pageable) {
        return searchService.integratedSearchByTitle(target, title, pageable);
    }
}
