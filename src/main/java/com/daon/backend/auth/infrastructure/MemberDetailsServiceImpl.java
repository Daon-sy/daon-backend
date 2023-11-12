package com.daon.backend.auth.infrastructure;

import com.daon.backend.auth.service.MemberDetails;
import com.daon.backend.auth.service.MemberDetailsService;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberNotFoundException;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.member.dto.ModifyMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public ModifyMemberDto modify(UUID memberId, String email, String password, String name) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.byMemberId(memberId));
        member.modifyMember(email, password, name, passwordEncoder);

        return new ModifyMemberDto(member.getEmail(), member.getPassword(), member.getName());
    }
}
