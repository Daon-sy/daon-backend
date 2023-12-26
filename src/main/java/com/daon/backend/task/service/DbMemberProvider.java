package com.daon.backend.task.service;

import com.daon.backend.task.dto.workspace.SearchMemberResponseDto;

import java.util.List;

public interface DbMemberProvider {

    String getMemberIdByUsername(String username);

    List<SearchMemberResponseDto.MemberSummary> searchMemberByUsername(String username, Long workspaceId);
}
