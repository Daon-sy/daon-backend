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
    public MemberDetails signIn(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> MemberNotFoundException.byEmail(email));
        member.checkPassword(password, passwordEncoder);

        return new MemberDetails(member.getId().toString(), member.getEmail(), member.getName());
    }
}
