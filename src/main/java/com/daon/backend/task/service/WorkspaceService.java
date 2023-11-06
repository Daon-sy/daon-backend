package com.daon.backend.task.service;

import com.daon.backend.task.domain.workspace.Profile;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceCreator;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.dto.request.CheckJoinCodeRequestDto;
import com.daon.backend.task.dto.request.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.request.JoinWorkspaceRequestDto;
import com.daon.backend.task.dto.response.JoinWorkspaceResponseDto;
import com.daon.backend.task.dto.response.WorkspaceListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final SessionMemberProvider sessionMemberProvider;

    @Transactional
    public Long createWorkspace(CreateWorkspaceRequestDto requestDto) {
        Workspace workspace = Workspace.createOfGroup(
                requestDto.getWorkspace().getName(),
                requestDto.getWorkspace().getDescription(),
                requestDto.getWorkspace().getImageUrl(),
                requestDto.getWorkspace().getSubject(),
                new WorkspaceCreator(
                        sessionMemberProvider.getMemberId(),
                        requestDto.getProfile().getName(),
                        requestDto.getProfile().getImageUrl(),
                        requestDto.getProfile().getEmail()
                )
        );

        return workspaceRepository.save(workspace).getId();
    }

    public WorkspaceListResponseDto findAllWorkspace() {
        String memberId = sessionMemberProvider.getMemberId();
        return new WorkspaceListResponseDto(
                workspaceRepository.findWorkspacesByMemberId(memberId).stream()
                        .map(WorkspaceListResponseDto.WorkspaceSummary::new)
                        .collect(Collectors.toList())
        );
    }

    public void checkJoinCode(CheckJoinCodeRequestDto requestDto) {
        String requestedJoinCode = requestDto.getJoinCode();
        // TODO 초대링크 도입 후 수정
        Workspace workspace = workspaceRepository.findWorkspaceByJoinCode(requestedJoinCode)
                .orElseThrow();
        workspace.checkJoinCode(requestedJoinCode);
    }

    @Transactional
    public JoinWorkspaceResponseDto joinWorkspace(JoinWorkspaceRequestDto requestDto) {
        String requestedJoinCode = requestDto.getJoinCode();
        // TODO 초대링크 도입 후 수정
        Workspace workspace = workspaceRepository.findWorkspaceByJoinCode(requestedJoinCode)
                .orElseThrow();

        String memberId = sessionMemberProvider.getMemberId();
        Profile profile = new Profile(
                requestDto.getProfile().getName(),
                requestDto.getProfile().getImageUrl(),
                requestDto.getProfile().getEmail()
        );
        workspace.addParticipant(memberId, profile);

        return new JoinWorkspaceResponseDto(workspace.getId());
    }
}
