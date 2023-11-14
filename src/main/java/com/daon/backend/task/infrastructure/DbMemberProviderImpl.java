package com.daon.backend.task.infrastructure;

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
    public String getMemberIdByEmail(String email) {
        return null;
//        return memberRepository.findByEmail(email)
//                .orElseThrow(() -> MemberNotFoundException.byUsername(email))
//                .getId().toString();
    }
}
