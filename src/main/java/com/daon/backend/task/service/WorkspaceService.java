package com.daon.backend.task.service;

import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.request.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.request.InviteMemberRequestDto;
import com.daon.backend.task.dto.request.ModifyRoleRequestDto;
import com.daon.backend.task.dto.request.ModifyWorkspaceRequestDto;
import com.daon.backend.task.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final SessionMemberProvider sessionMemberProvider;
    private final DbMemberProvider dbMemberProvider;

    @Transactional
    public CreateWorkspaceResponseDto createWorkspace(CreateWorkspaceRequestDto requestDto) {
        Workspace workspace = Workspace.createOfGroup(
                requestDto.getWorkspace().getTitle(),
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

        Long workspaceId = workspaceRepository.save(workspace).getId();

        return new CreateWorkspaceResponseDto(workspaceId);
    }

    public WorkspaceListResponseDto findAllWorkspace() {
        String memberId = sessionMemberProvider.getMemberId();
        return new WorkspaceListResponseDto(
                workspaceRepository.findWorkspacesByMemberId(memberId).stream()
                        .map(WorkspaceListResponseDto.WorkspaceSummary::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void createPersonalWorkspace(WorkspaceCreator workspaceCreator) {
        Workspace personalWorkspace = Workspace.createOfPersonal(workspaceCreator);
        workspaceRepository.save(personalWorkspace);
    }

    public FindProfileResponseDto findProfile(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace findWorkspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant findWorkspaceParticipant = workspaceRepository.findWorkspaceParticipantByWorkspaceAndMemberId(findWorkspace, memberId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(memberId, findWorkspace.getId()));

        return new FindProfileResponseDto(findWorkspaceParticipant);
    }

    public FindWorkspaceParticipantsResponseDto findWorkspaceParticipants(Long workspaceId) {
        List<WorkspaceParticipant> findWorkspaceParticipants =
                workspaceRepository.findWorkspaceParticipantsByWorkspaceId(workspaceId);

        return new FindWorkspaceParticipantsResponseDto(
                findWorkspaceParticipants.stream()
                        .map(FindWorkspaceParticipantsResponseDto.WorkspaceParticipantProfile::new)
                        .collect(Collectors.toList())
        );
    }

    public CheckRoleResponseDto findParticipantRole(Long workspaceId, String memberId) {
        Role findRole = workspaceRepository.findParticipantRoleByMemberIdAndWorkspaceId(memberId, workspaceId);

        return new CheckRoleResponseDto(findRole);
    }

    public boolean isWorkspaceParticipants(Long workspaceId, String memberId) {
        Workspace findWorkspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        return findWorkspace.isWorkspaceParticipantsByMemberId(memberId);
    }

    @Transactional
    public void inviteMember(Long workspaceId, InviteMemberRequestDto requestDto) {
        String memberId = dbMemberProvider.getMemberIdByEmail(requestDto.getEmail());
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        workspace.addParticipant(
                memberId,
                new Profile(
                        String.valueOf(workspaceId),
                        null,
                        null
                )
        );
    }

    @Transactional
    public void modifyWorkspace(ModifyWorkspaceRequestDto requestDto, Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        workspace.modifyWorkspace(requestDto.getTitle(), requestDto.getDescription(), requestDto.getImageUrl(), requestDto.getSubject());
    }

    @Transactional
    public void modifyParticipantRole(ModifyRoleRequestDto requestDto, Long workspaceId) {
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        Workspace findWorkspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant workspaceParticipant = findWorkspace.findWorkspaceParticipant(workspaceParticipantId, workspaceId);
        workspaceParticipant.modifyRole(requestDto.getRole());
    }
}
