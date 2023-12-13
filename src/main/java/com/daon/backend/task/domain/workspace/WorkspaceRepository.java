package com.daon.backend.task.domain.workspace;

import com.daon.backend.task.dto.WorkspaceSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Optional<Workspace> findWorkspaceById(Long workspaceId);

    List<Workspace> findWorkspacesByMemberId(String memberId);

    List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId);

    Role findParticipantRoleByMemberIdAndWorkspaceId(String memberId, Long workspaceId);

    Slice<WorkspaceSummary> searchWorkspaceSummariesByTitle(String memberId, String title, Pageable pageable);

    void deleteAllRelatedWorkspace(Long workspaceId);

    void deleteAllRelatedWorkspaceParticipant(Long workspaceParticipantId, String memberId);

    Page<Message> findMessages(Workspace workspace, Long receiverId, String target, String keyword, Pageable pageable);

    void readAllMessages(Long workspaceId, Long receiverId);

    void deleteAllMessagesRelatedWorkspaceParticipant(Long workspaceParticipantId);

    void deleteAllReplyWriterRelatedMemberId(Long workspaceParticipantId, String memberId);
}
