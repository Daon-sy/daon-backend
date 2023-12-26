package com.daon.backend.auth.infrastructure;

import com.daon.backend.auth.service.MemberDetails;
import com.daon.backend.auth.service.MemberDetailsService;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsServiceImpl implements MemberDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MemberDetails signIn(String username, String password) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> MemberNotFoundException.byUsername(username));
        member.checkPassword(password, passwordEncoder);

        return new MemberDetails(member.getId(), member.getUsername(), member.getName());
    }
}
