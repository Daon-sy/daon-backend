package com.daon.backend.task.service;

import com.daon.backend.common.response.slice.PageResponse;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.domain.workspace.exception.CanNotDeletePersonalWorkspaceException;
import com.daon.backend.task.domain.workspace.exception.CanNotModifyMyRoleException;
import com.daon.backend.task.domain.workspace.exception.WorkspaceNotFoundException;
import com.daon.backend.task.dto.WorkspaceSummary;
import com.daon.backend.task.dto.workspace.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    private final ProjectRepository projectRepository;
    private final SessionMemberProvider sessionMemberProvider;
    private final DbMemberProvider dbMemberProvider;

    /**
     * 워크스페이스 생성
     */
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

        return new CreateWorkspaceResponseDto(workspaceRepository.save(workspace).getId());
    }

    /**
     * 워크스페이스 목록 조회
     */
    public FindWorkspacesResponseDto findWorkspaces() {
        String memberId = sessionMemberProvider.getMemberId();

        return new FindWorkspacesResponseDto(
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

    /**
     * 워크스페이스 참여자 목록 조회
     */
    public FindWorkspaceParticipantsResponseDto findWorkspaceParticipants(Long workspaceId) {
        List<WorkspaceParticipant> workspaceParticipants =
                workspaceRepository.findWorkspaceParticipantsByWorkspaceId(workspaceId);

        return new FindWorkspaceParticipantsResponseDto(
                workspaceParticipants.stream()
                        .map(FindWorkspaceParticipantsResponseDto.WorkspaceParticipantProfileDetail::new)
                        .collect(Collectors.toList())
        );
    }

    public CheckRoleResponseDto findParticipantRole(Long workspaceId, String memberId) {
        Role role = workspaceRepository.findParticipantRoleByMemberIdAndWorkspaceId(memberId, workspaceId);

        return new CheckRoleResponseDto(role);
    }

    public boolean isWorkspaceParticipants(Long workspaceId, String memberId) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        return workspace.isWorkspaceParticipantsByMemberId(memberId);
    }

    /**
     * 워크스페이스 수정
     */
    @Transactional
    public void modifyWorkspace(ModifyWorkspaceRequestDto requestDto, Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        workspace.modifyWorkspace(
                requestDto.getTitle(),
                requestDto.getDescription(),
                requestDto.getImageUrl(),
                requestDto.getSubject()
        );
    }

    /**
     * 워크스페이스 참여자 권한 변경
     */
    @Transactional
    public void modifyParticipantRole(ModifyRoleRequestDto requestDto, Long workspaceId) {
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant workspaceParticipant =
                workspace.findWorkspaceParticipantByWorkspaceParticipantId(workspaceParticipantId, workspaceId);

        String memberIdWhoRequest = sessionMemberProvider.getMemberId();
        if (workspaceParticipant.getMemberId().equals(memberIdWhoRequest)) {
            throw new CanNotModifyMyRoleException(memberIdWhoRequest);
        }
        workspaceParticipant.modifyRole(requestDto.getRole());
    }

    /**
     * 프로필(본인) 수정
     */
    @Transactional
    public void modifyProfile(Long workspaceId, ModifyProfileRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        WorkspaceParticipant workspaceParticipant = workspace.findWorkspaceParticipantByMemberId(memberId);
        workspaceParticipant.getProfile().modifyProfile(
                requestDto.getName(),
                requestDto.getImageUrl(),
                requestDto.getEmail()
        );
    }

    /**
     * 프로필(본인) 조회
     */
    public FindProfileResponseDto findProfile(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace findWorkspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant workspaceParticipant = findWorkspace.findWorkspaceParticipantByMemberId(memberId);

        return new FindProfileResponseDto(workspaceParticipant);
    }

    /**
     * 워크스페이스 단건 조회
     */
    public FindWorkspaceResponseDto findWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        return new FindWorkspaceResponseDto(workspace);
    }

    /**
     * 워크스페이스 초대
     */
    @Transactional
    public void inviteMember(Long workspaceId, InviteMemberRequestDto requestDto) {
        String invitedMemberId = dbMemberProvider.getMemberIdByUsername(requestDto.getUsername());
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        workspace.addWorkspaceInvitation(invitedMemberId, new WorkspaceInvitation(invitedMemberId, workspace, requestDto.getRole()));
    }

    /**
     * 워크스페이스 참여
     */
    @Transactional
    public void joinWorkspace(Long workspaceId, JoinWorkspaceRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        workspace.addParticipant(
                memberId,
                new Profile(
                        requestDto.getName(),
                        requestDto.getImageUrl(),
                        requestDto.getEmail()
                )
        );
    }

    /**
     * 워크스페이스 탈퇴
     */
    @Transactional
    public void withdrawWorkspace(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        if (workspace.canWithdrawWorkspace(memberId)) {
            Long workspaceParticipantId = workspace.findWorkspaceParticipantByMemberId(memberId).getId();
            workspaceRepository.deleteAllReplyWriterRelatedMemberId(workspaceParticipantId, memberId);
            workspaceRepository.deleteAllMessagesRelatedWorkspaceParticipant(workspaceParticipantId);
            workspaceRepository.deleteAllRelatedWorkspaceParticipant(workspaceParticipantId, memberId);
            workspace.withdrawWorkspace(memberId);
        } else {
            deleteWorkspace(workspaceId);
        }
    }

    /**
     * 워크스페이스 참여자 강퇴
     */
    @Transactional
    public void deportWorkspaceParticipant(Long workspaceId, DeportWorkspaceParticipantRequestDto requestDto) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        WorkspaceParticipant workspaceParticipant = workspace.getWorkspaceParticipant(workspaceParticipantId);
        String workspaceParticipantMemberId = workspaceParticipant.getMemberId();

        workspaceParticipant.getParticipants().forEach(
                projectParticipant ->
                        projectRepository.deleteAllTaskBookmarkRelatedProjectParticipant(projectParticipant.getId()
                        )
        );
        workspaceRepository.deleteAllMessagesRelatedWorkspaceParticipant(workspaceParticipantId);
        workspaceRepository.deleteAllReplyWriterRelatedMemberId(workspaceParticipantId, workspaceParticipantMemberId);
        workspaceRepository.deleteAllRelatedWorkspaceParticipant(workspaceParticipantId, workspaceParticipantMemberId);
        workspace.deportWorkspace(workspaceParticipantId, workspaceParticipantMemberId);
    }

    /**
     * 워크스페이스 삭제
     */
    @Transactional
    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        if (workspace.isPersonal()) {
            throw new CanNotDeletePersonalWorkspaceException(workspaceId);
        }

        workspace.getWorkspaceParticipants().forEach(
                workspaceParticipant -> workspaceParticipant.getParticipants().forEach(
                        projectParticipant ->
                                projectRepository.deleteAllTaskBookmarkRelatedProjectParticipant(
                                        projectParticipant.getId()
                                )
                )
        );
        workspace.deleteWorkspace();
        workspaceRepository.deleteAllRelatedWorkspace(workspaceId);
    }

    /**
     * 워크스페이스 초기화
     */
    @Transactional
    public void resetWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        String memberId = sessionMemberProvider.getMemberId();
        workspace.resetWorkspace(memberId);
    }

    /**
     * 회원 검색
     */
    public SearchMemberResponseDto searchMember(Long workspaceId, String username) {
        return new SearchMemberResponseDto(dbMemberProvider.searchMemberByUsername(username, workspaceId));
    }

    /**
     * 쪽지 보내기
     */
    @Transactional
    public void createMessage(Long workspaceId, SendMessageRequestDto requestDto) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        WorkspaceParticipant receiver = workspace.findWorkspaceParticipantByWorkspaceParticipantId(
                requestDto.getWorkspaceParticipantId(),
                workspaceId
        );
        WorkspaceParticipant sender = workspace.findWorkspaceParticipantByMemberId(memberId);

        workspace.checkMessageCanBeSend(sender.getId(), receiver.getId());
        workspace.saveMessage(
                requestDto.getTitle(),
                requestDto.getContent(),
                receiver,
                sender
        );
    }

    /**
     * 쪽지 단건 조회
     */
    @Transactional
    public FindMessageResponseDto findMessage(Long workspaceId, Long messageId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(memberId).getId();

        Message message = workspace.findMessage(messageId, receiverId);
        message.readMessage();

        Long senderId = message.getSenderId();
        WorkspaceParticipant sender = workspace.findWorkspaceParticipantForMessage(senderId);

        return new FindMessageResponseDto(message, sender);
    }

    /**
     * 쪽지 목록 조회
     */
    public PageResponse<MessageSummary> findMessages(Long workspaceId, String target, String keyword, Pageable pageable) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(memberId).getId();

        return new PageResponse<>(
                workspaceRepository.findMessages(workspace, receiverId, target, keyword, pageable)
                        .map(message ->
                                new MessageSummary(
                                        message,
                                        workspace.findWorkspaceParticipantForMessage(message.getSenderId())
                                )
                        )
        );
    }

    /**
     * 쪽지 삭제
     */
    @Transactional
    public void deleteMessage(Long workspaceId, Long messageId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(memberId).getId();

        workspace.deleteMessage(messageId, receiverId);
    }

    /**
     * 쪽지 모두 읽기
     */
    @Transactional
    public void readAllMessages(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(memberId).getId();

        workspaceRepository.readAllMessages(workspaceId, receiverId);
    }
}
