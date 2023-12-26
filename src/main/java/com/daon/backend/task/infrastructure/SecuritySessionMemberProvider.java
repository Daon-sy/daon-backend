package com.daon.backend.task.infrastructure;

import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.security.SecurityUtils;
import com.daon.backend.task.service.SessionMemberProvider;
import org.springframework.stereotype.Service;

@Service
public class SecuritySessionMemberProvider implements SessionMemberProvider {

    @Override
    public String getMemberId() {
        MemberPrincipal memberPrincipal = SecurityUtils.getMemberPrincipal();
        return memberPrincipal.getMemberId();
    }
}
