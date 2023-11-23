package com.daon.backend.task.service;

import com.daon.backend.task.domain.board.Board;
import com.daon.backend.task.domain.project.Project;
import com.daon.backend.task.domain.project.ProjectParticipant;
import com.daon.backend.task.domain.project.ProjectRepository;
import com.daon.backend.task.domain.task.Task;
import com.daon.backend.task.domain.task.TaskRepository;
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
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

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

        Long workspaceId = workspaceRepository.save(workspace).getId();

        return new CreateWorkspaceResponseDto(workspaceId);
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

    /**
     * 워크스페이스 수정
     */
    @Transactional
    public void modifyWorkspace(ModifyWorkspaceRequestDto requestDto, Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        workspace.modifyWorkspace(requestDto.getTitle(), requestDto.getDescription(), requestDto.getImageUrl(), requestDto.getSubject());
    }

    /**
     * 워크스페이스 참여자 권한 변경
     */
    @Transactional
    public void modifyParticipantRole(ModifyRoleRequestDto requestDto, Long workspaceId) {
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        Workspace workspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        WorkspaceParticipant workspaceParticipant = workspace.findWorkspaceParticipantByWorkspaceParticipantId(workspaceParticipantId, workspaceId);
        workspaceParticipant.modifyRole(requestDto.getRole());
    }

    /**
     * 프로필(본인) 수정
     */
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

    /**
     * 프로필(본인) 조회
     */
    public FindProfileResponseDto findProfile(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace findWorkspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        WorkspaceParticipant workspaceParticipant = findWorkspace.findWorkspaceParticipantByMemberId(memberId);
        return new FindProfileResponseDto(workspaceParticipant);
    }

    /**
     * 워크스페이스 단 건 조회
     */
    public FindWorkspaceResponseDto findWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        return new FindWorkspaceResponseDto(workspace);
    }

    /**
     * 워크스페이스 초대
     */
    @Transactional
    public void inviteMember(Long workspaceId, InviteMemberRequestDto requestDto) {
        String memberId = dbMemberProvider.getMemberIdByUsername(requestDto.getUsername());
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));

        workspace.addWorkspaceInvitation(new WorkspaceInvitation(memberId, workspace));
    }

    /**
     * 워크스페이스 참여
     */
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

    /**
     * 워크스페이스 탈퇴
     */
    @Transactional
    public void withdrawWorkspace(Long workspaceId) {
        String memberId = sessionMemberProvider.getMemberId();
        Workspace workspace = workspaceRepository.findWorkspaceWithParticipantsByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        Long workspaceParticipantId = workspace.findWorkspaceParticipantByMemberId(memberId).getId();

        List<WorkspaceParticipant> workspaceParticipants = workspace.getParticipants();
        long adminCount = workspaceParticipants.stream()
                .filter(workspaceParticipant -> Role.WORKSPACE_ADMIN.equals(workspaceParticipant.getRole()))
                .count();
        if (adminCount == 1) {
            deleteWorkspace(workspaceId);
        } else {
            workspace.getParticipants().stream()
                    .filter(workspaceParticipant -> workspaceParticipant.getMemberId().equals(memberId))
                    .forEach(workspaceParticipant -> projectRepository.findAllProjectsByWorkspaceParticipant(workspaceParticipant).stream()
                            .peek(project -> {
                                List<Task> tasks = taskRepository.findAllTasksByProjectId(project.getId());
                                tasks.stream()
                                        .filter(task -> {
                                            ProjectParticipant taskManager = task.getTaskManager();
                                            return taskManager != null && taskManager.getMemberId().equals(memberId);
                                        })
                                        .forEach(Task::deleteTaskManager);
                                tasks.stream()
                                        .filter(task -> task.getCreatorId().equals(workspaceParticipantId))
                                        .forEach(Task::deleteCreator);
                            })
                            .forEach(project -> project.withdrawProject(memberId))
                    );

            workspace.withdrawWorkspace(memberId);
        }
    }

    /**
     * 워크스페이스 참여자 강퇴
     */
    @Transactional
    public void deportWorkspaceParticipant(Long workspaceId, DeportWorkspaceParticipantRequestDto requestDto) {
        Long workspaceParticipantId = requestDto.getWorkspaceParticipantId();
        WorkspaceParticipant workspaceParticipant = workspaceRepository.findWorkspaceParticipantByWorkspaceParticipantId(workspaceParticipantId)
                .orElseThrow(() -> new NotWorkspaceParticipantException(workspaceId));
        String workspaceParticipantMemberId = workspaceParticipant.getMemberId();

        List<Project> projects = projectRepository.findProjectsByWorkspaceParticipantId(workspaceParticipantId);
        projects.stream()
                .peek(project -> {
                    List<Task> tasks = taskRepository.findAllTasksByProjectId(project.getId());
                    tasks.stream()
                            .filter(task -> {
                                ProjectParticipant taskManager = task.getTaskManager();
                                return taskManager != null && taskManager.getMemberId().equals(workspaceParticipantMemberId);
                            })
                            .forEach(Task::deleteTaskManager);
                    tasks.stream()
                            .filter(task -> task.getCreatorId().equals(workspaceParticipantId))
                            .forEach(Task::deleteCreator);
                })
                .forEach(project -> project.withdrawProject(workspaceParticipantMemberId));

        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        workspace.deportWorkspace(workspaceParticipantId, workspaceParticipantMemberId);
    }

    /**
     * 워크스페이스 삭제
     */
    @Transactional
    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findWorkspaceByWorkspaceId(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException(workspaceId));
        if (workspace.getDivision().equals(Division.PERSONAL)) {
            throw new CanNotDeletePersonalWorkspaceException(workspaceId);
        }

        List<Project> projects = projectRepository.findAllProjectsByWorkspaceId(workspaceId);
        projects.stream()
                .peek(project -> {
                    taskRepository.findAllTasksByProjectId(project.getId())
                            .forEach(Task::deleteTask);
                    project.getBoards().forEach(Board::deleteBoard);
                })
                .forEach(Project::deleteProject);

        workspace.deleteWorkspace();
    }
}
