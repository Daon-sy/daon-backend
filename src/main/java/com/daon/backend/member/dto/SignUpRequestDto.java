package com.daon.backend.member.dto;

import com.daon.backend.member.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String name;

    @Builder
    public SignUpRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
