package com.daon.backend.task.service;

import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.WorkspaceSummary;
import com.daon.backend.task.dto.workspace.*;
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
                        .map(WorkspaceSummary::new)
                        .collect(Collectors.toList())
        );
    }

    @Transactional
    public void createPersonalWorkspace(String memberId, String name, String email) {
        Workspace workspace = Workspace.createOfPersonal(
                new WorkspaceCreator(
                        memberId,
                        name,
                        null,
                        email
                )
        );
        workspaceRepository.save(workspace);
    }

    public FindWorkspaceParticipantsResponseDto findWorkspaceParticipants(Long workspaceId) {
        List<WorkspaceParticipant> workspaceParticipants =
                workspaceRepository.findWorkspaceParticipantsByWorkspaceId(workspaceId);

        return new FindWorkspaceParticipantsResponseDto(
                workspaceParticipants.stream()
                        .map(FindWorkspaceParticipantsResponseDto.WorkspaceParticipantProfile::new)
                        .collect(Collectors.toList())
        );
    }

    public CheckRoleResponseDto findParticipantRole(Long workspaceId, String memberId) {
        Role role = workspaceRepository.findParticipantRoleByMemberIdAndWorkspaceId(memberId, workspaceId);

        return new CheckRoleResponseDto(role);
    }

    public boolean isWorkspaceParticipants(Long workspaceId, String memberId) {
        Workspace workspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        return workspace.isWorkspaceParticipantsByMemberId(memberId);
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
        Workspace workspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant workspaceParticipant = workspace.findWorkspaceParticipantByWorkspaceParticipantId(workspaceParticipantId, workspaceId);
        workspaceParticipant.modifyRole(requestDto.getRole());
    }

    @Transactional
    public void modifyProfile(Long workspaceId, ModifyProfileRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace findWorkspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        WorkspaceParticipant workspaceParticipant = findWorkspace.findWorkspaceParticipantByMemberId(memberId);
        workspaceParticipant.getProfile().modifyProfile(
                requestDto.getName(),
                requestDto.getImageUrl(),
                requestDto.getEmail()
        );
    }

    public FindProfileResponseDto findProfile(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace findWorkspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        WorkspaceParticipant workspaceParticipant = findWorkspace.findWorkspaceParticipantByMemberId(memberId);
        return new FindProfileResponseDto(workspaceParticipant);
    }

    public FindWorkspaceResponseDto findWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        return new FindWorkspaceResponseDto(workspace);
    }

    @Transactional
    public void inviteMember(Long workspaceId, InviteMemberRequestDto requestDto) {
        String memberId = dbMemberProvider.getMemberIdByUsername(requestDto.getUsername());
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        workspace.addWorkspaceInvitation(new WorkspaceInvitation(memberId, workspace));
    }

    @Transactional
    public void joinWorkspace(Long workspaceId, JoinWorkspaceRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        if (workspace.checkInvitedMember(memberId)) {
            workspace.addParticipant(
                    memberId,
                    new Profile(
                            requestDto.getName(),
                            requestDto.getImageUrl(),
                            requestDto.getEmail()
                    )
            );
            workspace.removeWorkspaceInvitation(memberId);
        } else {
            throw new NotInvitedMemberException(workspaceId, memberId);
        }
    }
}
