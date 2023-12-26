package com.daon.backend.member.infrastructure;

import com.daon.backend.member.service.SessionMemberProvider;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.security.SecurityUtils;
import org.springframework.stereotype.Service;

@Service("memberSecuritySessionMemberProvider")
public class SecuritySessionMemberProvider implements SessionMemberProvider {

    @Override
    public String getMemberId() {
        MemberPrincipal memberPrincipal = SecurityUtils.getMemberPrincipal();
        return memberPrincipal.getMemberId();
    }
}
