package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.domain.workspace.exception.WorkspaceNotFoundException;
import com.daon.backend.task.dto.WorkspaceNoticeSummary;
import com.daon.backend.task.dto.workspace.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        Workspace workspace = workspaceRepository.findById(workspaceId)
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
    public PageResponse<WorkspaceNoticeSummary> findWorkspaceNotices(Long workspaceId, String keyword, Pageable pageable) {
        Page<WorkspaceNotice> workspaceNotices;

        if (keyword != null && !keyword.isEmpty()) {
            workspaceNotices = workspaceNoticeRepository.findWorkspaceNoticesByWorkspaceIdAndKeyword(workspaceId, keyword, pageable);
        } else {
            workspaceNotices = workspaceNoticeRepository.findWorkspaceNoticesByWorkspaceId(workspaceId, pageable);
        }

        List<WorkspaceNoticeSummary> noticeSummaries = workspaceNotices.getContent().stream()
                .map(WorkspaceNoticeSummary::new)
                .collect(Collectors.toList());

        return new PageResponse<>(new PageImpl<>(noticeSummaries, pageable, workspaceNotices.getTotalElements()));
    }


    /**
     * 워크스페이스 공지사항 단건 조회
     * */
    public FindWorkspaceNoticeResponseDto findWorkspaceNotice(Long noticeId){
        WorkspaceNotice workspaceNotice = workspaceNoticeRepository.findById(noticeId)
                .orElseThrow(() -> new WorkspaceNoticeNotFoundException(noticeId));

        return new FindWorkspaceNoticeResponseDto(workspaceNotice);
    }

    /**
     * 워크스페이스 공지사항 수정
     * */
    @Transactional
    public void modifyWorkspaceNoticeContent(Long workspaceId, Long noticeId, ModifyWorkspaceNoticeRequestDto requestDto){
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        String memberId = sessionMemberProvider.getMemberId();
        WorkspaceParticipant workspaceNoticeWriter = workspace.findWorkspaceParticipantByMemberId(memberId);
        WorkspaceNotice workspaceNotice = workspace.findWorkspaceNoticeById(noticeId);
        workspaceNotice.modifyWorkspaceNotice(workspaceNoticeWriter,requestDto.getTitle(), requestDto.getContent());
    }

    /**
     * 워크스페이스 공지사항 삭제
     * */
    @Transactional
    public void deleteWorkspaceNotice(Long workspaceId, Long noticeId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        workspace.removeWorkspaceNotice(noticeId);
    }
}