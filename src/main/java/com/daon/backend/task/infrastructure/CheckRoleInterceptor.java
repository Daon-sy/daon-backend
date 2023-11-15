package com.daon.backend.task.infrastructure;

import com.daon.backend.auth.domain.UnauthenticatedMemberException;
import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.domain.authority.UnAuthorizedMemberException;
import com.daon.backend.task.domain.project.NotProjectParticipantException;
import com.daon.backend.task.domain.workspace.NotWorkspaceParticipantException;
import com.daon.backend.task.dto.workspace.CheckRoleResponseDto;
import com.daon.backend.task.service.ProjectService;
import com.daon.backend.task.service.SessionMemberProvider;
import com.daon.backend.task.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class CheckRoleInterceptor implements HandlerInterceptor {

    private final WorkspaceService workspaceService;
    private final ProjectService projectService;
    private final SessionMemberProvider sessionMemberProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        CheckRole checkRole = handlerMethod.getMethodAnnotation(CheckRole.class);
        if (checkRole == null) {
            return true;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthenticatedMemberException();
        }

        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long workspaceId = Long.valueOf(pathVariables.get("workspaceId"));
        String memberId = sessionMemberProvider.getMemberId();

        checkParticipants(workspaceId, memberId, pathVariables);
        checkRole(workspaceId, memberId, checkRole);

        return true;
    }

    private void checkRole(Long workspaceId, String memberId, CheckRole checkRole) {
        CheckRoleResponseDto checkRoleResponseDto = workspaceService.findParticipantRole(workspaceId, memberId);
        Set<Authority> memberAuthorities = new HashSet<>(checkRoleResponseDto.getRole().getAuthorities());
        Set<Authority> requiredAuthorities = new HashSet<>(List.of(checkRole.authority()));
        if (!memberAuthorities.containsAll(requiredAuthorities)) {
            throw new UnAuthorizedMemberException(requiredAuthorities);
        }
    }

    private void checkParticipants(Long workspaceId, String memberId, Map<String, String> pathVariables) {
        if (!workspaceService.isWorkspaceParticipants(workspaceId, memberId)) {
            throw new NotWorkspaceParticipantException(memberId, workspaceId);
        }
        if (pathVariables.containsKey("projectId")) {
            Long projectId = Long.valueOf(pathVariables.get("projectId"));
            if (!projectService.isProjectParticipants(projectId, memberId)) {
                throw new NotProjectParticipantException(memberId, workspaceId);
            }
        }
    }
}
