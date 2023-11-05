package com.daon.backend.member.dto;

import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.PasswordEncoder;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {

    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @Builder
    public SignUpRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .passwordEncoder(passwordEncoder)
                .build();
    }
}
