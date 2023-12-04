package com.daon.backend.task.service;

import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeResponseDto;
import com.daon.backend.task.dto.workspace.FindWorkspaceNoticeResponseDto;
import com.daon.backend.task.dto.workspace.FindWorkspaceNoticesResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceNoticeService{

    private final WorkspaceRepository workspaceRepository;
    private final SessionMemberProvider sessionMemberProvider;
    private final WorkspaceNoticeRepository workspaceNoticeRepository;

    /**
    *  워크스페이스 공지사항 생성
    * */
    @Transactional
    public CreateWorkspaceNoticeResponseDto createWorkspaceNotice(Long workspaceId, CreateWorkspaceNoticeRequestDto requestDto){
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant workspaceNoticeWriter = workspace.findWorkspaceParticipantByMemberId(memberId);

        WorkspaceNotice workspaceNotice = WorkspaceNotice.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .workspace(workspace)
                .workspaceNoticeWriter(workspaceNoticeWriter)
                .build();

        Long workspaceNoticeId = workspaceNoticeRepository.save(workspaceNotice).getId();
        return new CreateWorkspaceNoticeResponseDto(workspaceNoticeId);
    }

    /**
    * 워크스페이스 공지사항 목록 조회
    * */
    public FindWorkspaceNoticesResponseDto findWorkspaceNotices(Long workspaceId){
        List<WorkspaceNotice> workspaceNotices = workspaceNoticeRepository.findWorkspacesByWorkspaceId(workspaceId);

        return new FindWorkspaceNoticesResponseDto(
                workspaceNotices.stream()
                        .map(WorkspaceNoticeSummary::new)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 워크스페이스 공지사항 단건 조회
     * */
    public FindWorkspaceNoticeResponseDto findWorkspaceNotice(Long noticeId){
        WorkspaceNotice workspaceNotice = workspaceNoticeRepository.findWorkspaceNoticeById(noticeId)
                .orElseThrow(() -> new WorkspaceNoticeNotFoundException(noticeId));

        return new FindWorkspaceNoticeResponseDto(workspaceNotice);
    }
}