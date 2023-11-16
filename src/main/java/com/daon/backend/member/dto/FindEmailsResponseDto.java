package com.daon.backend.member.dto;

import com.daon.backend.member.domain.Email;
import com.daon.backend.task.domain.project.Board;
import com.daon.backend.task.dto.project.FindBoardsResponseDto;
import lombok.Getter;

import java.util.List;

@Getter
public class FindEmailsResponseDto {

    private int totalCount;

    private List<EmailInfo> emails;

    public FindEmailsResponseDto(List<EmailInfo> emails) {
        this.totalCount = emails.size();
        this.emails = emails;
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
