package com.daon.backend.task.dto;

import com.daon.backend.member.domain.Member;
import com.daon.backend.task.domain.Workspace;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ListWorkspaceRequestDto {

    private String email;
    private String password;
    private String name;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
