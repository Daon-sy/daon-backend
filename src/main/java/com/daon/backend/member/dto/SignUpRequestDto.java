package com.daon.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    @NotBlank
    @Length(max = 20)
    private String username;

    @NotBlank
    @Length(max = 30)
    private String password;

    @NotBlank
    @Length(max = 20)
    private String name;

    @Email
    private String email;

}
