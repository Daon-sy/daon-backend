package com.daon.backend.task.service;

import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.request.CheckJoinCodeRequestDto;
import com.daon.backend.task.dto.request.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.request.InviteMemberRequestDto;
import com.daon.backend.task.dto.request.JoinWorkspaceRequestDto;
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
    public Long createWorkspace(CreateWorkspaceRequestDto requestDto) {
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
        Long workspaceId = workspace.getId();

        String memberId = sessionMemberProvider.getMemberId();
        if (workspaceRepository.existsWorkspaceParticipantByMemberIdAndWorkspaceId(memberId, workspaceId)) {
            throw new SameMemberExistsException(memberId);
        }

        Profile profile = new Profile(
                requestDto.getProfile().getName(),
                requestDto.getProfile().getImageUrl(),
                requestDto.getProfile().getEmail()
        );
        workspace.addParticipant(memberId, profile);

        return new JoinWorkspaceResponseDto(workspaceId);
    }

    @Transactional
    public void createPersonalWorkspace(WorkspaceCreator workspaceCreator) {
        Workspace personalWorkspace = Workspace.createOfPersonal(workspaceCreator);
        workspaceRepository.save(personalWorkspace);
    }

    public FindProfileResponseDto findProfile(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace findWorkspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant findWorkspaceParticipant = workspaceRepository.findWorkspaceParticipantByWorkspaceAndMemberId(findWorkspace, memberId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(memberId, findWorkspace.getId()));

        return new FindProfileResponseDto(findWorkspaceParticipant);
    }

    public FindParticipantsResponseDto findParticipants(Long workspaceId) {
        List<WorkspaceParticipant> findWorkspaceParticipants =
                workspaceRepository.findWorkspaceParticipantsByWorkspaceId(workspaceId);

        return new FindParticipantsResponseDto(
                findWorkspaceParticipants.stream()
                        .map(FindParticipantsResponseDto.ParticipantProfile::new)
                        .collect(Collectors.toList())
        );
    }

    public CheckRoleResponseDto findParticipantRole(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Role findRole = workspaceRepository.findParticipantRoleByMemberId(memberId, workspaceId);

        return new CheckRoleResponseDto(findRole);
    }

    @Transactional
    public void inviteMember(Long workspaceId, InviteMemberRequestDto requestDto) {
        String memberId = dbMemberProvider.getMemberIdByEmail(requestDto.getEmail());
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
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
}
