package com.daon.backend.task.service;

import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeRequestDto;
import com.daon.backend.task.dto.workspace.CreateWorkspaceNoticeResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}