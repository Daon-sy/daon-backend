package com.daon.backend.member.dto;

import com.daon.backend.member.domain.Email;
import lombok.Getter;

import java.util.List;

@Getter
public class FindEmailsResponseDto {

    private int totalCount;

    private List<EmailInfo> memberEmails;

    public FindEmailsResponseDto(List<EmailInfo> memberEmails) {
        this.totalCount = memberEmails.size();
        this.memberEmails = memberEmails;
    }

    @Getter
    public static class EmailInfo {
        private Long memberEmailId;

        private String email;

        public EmailInfo(Email email) {
            this.memberEmailId = email.getId();
            this.email = email.getEmail();
        }
    }
}
