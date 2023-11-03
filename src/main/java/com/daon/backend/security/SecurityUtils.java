package com.daon.backend.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static MemberPrincipal getMemberPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof MemberPrincipal) return (MemberPrincipal) principal;

        throw new MemberNotAuthenticatedException();
    }
}
