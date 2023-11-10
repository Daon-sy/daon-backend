package com.daon.backend.task.infrastructure;

import com.daon.backend.auth.domain.UnauthenticatedMemberException;
import com.daon.backend.task.domain.authority.Authority;
import com.daon.backend.task.domain.authority.CheckRole;
import com.daon.backend.task.domain.authority.UnAuthorizedMemberException;
import com.daon.backend.task.dto.response.CheckRoleResponseDto;
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

        CheckRoleResponseDto checkRoleResponseDto = workspaceService.findParticipantRole(workspaceId);
        Set<Authority> memberAuthorities = new HashSet<>(checkRoleResponseDto.getRole().getAuthorities());
        Set<Authority> requiredAuthorities = new HashSet<>(List.of(checkRole.authority()));
        if (!memberAuthorities.containsAll(requiredAuthorities)) {
            throw new UnAuthorizedMemberException(requiredAuthorities);
        }

        return true;
    }
}
