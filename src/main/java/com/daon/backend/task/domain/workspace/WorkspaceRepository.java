package com.daon.backend.task.domain.workspace;

import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WorkspaceRepository {

    Workspace save(Workspace workspace);

    Optional<Workspace> findById(Long workspaceId);

    List<Workspace> findWorkspacesByMemberId(String memberId);

    List<WorkspaceParticipant> findWorkspaceParticipantsByWorkspaceId(Long workspaceId);

    Role findParticipantRoleByMemberIdAndWorkspaceId(String memberId, Long workspaceId);

    Page<Message> findMessages(Workspace workspace, Long receiverId, String target, String keyword, Pageable pageable);

    Page<Message> findSendMessages(Workspace workspace, Long senderId, String target, String keyword, Pageable pageable);

    void readAllMessages(Long workspaceId, Long receiverId);

    void deleteById(Long workspaceId);

    void deleteTaskManager(Long workspaceParticipantId);

    void deleteMessages(Long workspaceParticipantId);

    void deleteTaskReplies(Long workspaceParticipantId);

    List<Project> findProjectBy(WorkspaceParticipant wsp);

    void flush();
}
