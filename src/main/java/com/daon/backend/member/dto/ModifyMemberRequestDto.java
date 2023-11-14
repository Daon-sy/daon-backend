package com.daon.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMemberRequestDto {

    @Length(max = 20)
    private String name;

    @Length(max = 30)
    private String newPassword;

    @NotBlank
    @Length(max = 30)
    private String prevPassword;

}
