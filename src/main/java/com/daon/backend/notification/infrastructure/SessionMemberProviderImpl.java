package com.daon.backend.notification.infrastructure;

import com.daon.backend.notification.service.SessionMemberProvider;
import com.daon.backend.security.SecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class SessionMemberProviderImpl implements SessionMemberProvider {

    @Override
    public String getMemberId() {
        return SecurityUtils.getMemberPrincipal().getMemberId();
    }
}
