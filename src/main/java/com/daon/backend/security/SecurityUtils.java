package com.daon.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static MemberPrincipal getMemberPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            throw new MemberNotAuthenticatedException();
        }

        return (MemberPrincipal) principal;
    }
}
