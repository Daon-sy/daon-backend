package com.daon.backend.task.infrastructure.workspace;

import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.task.service.DbMemberProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DbMemberProviderImpl implements DbMemberProvider {

    private final MemberRepository memberRepository;

    @Override
    public String getMemberIdByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> MemberNotFoundException.byUsername(username))
                .getId();
    }
}
