package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.dto.search.SearchResponseDto;
import com.daon.backend.task.infrastructure.SearchQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchQueryRepository searchQueryRepository;

    /**
     * 통합 검색
     */
    public SearchResponseDto integratedSearchByTitle(String keyword) {
        return new SearchResponseDto(
                searchQueryRepository.findWorkspacesByTitle(
                        keyword,
                        PageRequest.of(
                                0,
                                3,
                                Sort.by(List.of(Sort.Order.by("createdAt").with(Sort.Direction.DESC)))
                        )
                ),
                searchQueryRepository.findProjectsByTitle(
                        keyword,
                        PageRequest.of(
                                0,
                                3,
                                Sort.by(List.of(Sort.Order.by("createdAt").with(Sort.Direction.DESC)))
                        )
                ),
                searchQueryRepository.findTasksByTitle(
                        keyword,
                        PageRequest.of(
                                0,
                                5,
                                Sort.by(List.of(Sort.Order.by("createdAt").with(Sort.Direction.DESC)))
                        )
                )
        );
    }


    public PageResponse<SearchResponseDto.WorkspaceResult> searchWorkspaces(String keyword, Pageable pageable) {
        return new PageResponse<>(searchQueryRepository.findWorkspacesByTitle(keyword, pageable));
    }

    public PageResponse<SearchResponseDto.ProjectResult> searchProjects(String keyword, Pageable pageable) {
        return new PageResponse<>(searchQueryRepository.findProjectsByTitle(keyword, pageable));
    }

    public PageResponse<SearchResponseDto.TaskResult> searchTasks(String keyword, Pageable pageable) {
        return new PageResponse<>(searchQueryRepository.findTasksByTitle(keyword, pageable));
    }
}
