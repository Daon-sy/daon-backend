package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.task.domain.workspace.Message;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceRepository;
import com.daon.backend.task.domain.workspace.exception.MessageNotFoundException;
import com.daon.backend.task.dto.workspace.FindMessageResponseDto;
import com.daon.backend.task.dto.workspace.SendMessageRequestDto;
import com.daon.backend.task.infrastructure.workspace.MessageJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class MessageServiceTest extends MockConfig {

    @MockBean
    SessionMemberProvider sessionMemberProvider;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    MessageJpaRepository messageJpaRepository;

    @BeforeEach
    void setUp() {
        BDDMockito.given(sessionMemberProvider.getMemberId()).willReturn("78cfb9f6-ec40-4ec7-b5bd-b7654fa014f8");
    }

    @DisplayName("쪽지 보내기")
    @Test
    void createMessage() {
        // given
        Long workspaceId = 3L;
        Long receiverId = 4L;

        SendMessageRequestDto sendMessageRequestDto = new SendMessageRequestDto(
                "쪽지 제목",
                "쪽지 내용",
                receiverId
        );

        // when
        workspaceService.createMessage(workspaceId, sendMessageRequestDto);
        List<Message> messages = messageJpaRepository.findAll();

        // then
        assertEquals(3, messages.size());
    }

    @DisplayName("쪽지 단건 조회")
    @Test
    void findMessage() {
        // given
        Long workspaceId = 3L;
        Long messageId = 1L;

        // when
        FindMessageResponseDto responseDto = workspaceService.findMessage(workspaceId, messageId);

        // then
        assertEquals(messageId, responseDto.getMessageId());
        assertEquals("message title1", responseDto.getTitle());
        assertEquals("message content1", responseDto.getContent());
    }

    @DisplayName("쪽지 목록 조회")
    @Test
    void findMessages() {
        // given
        Long workspaceId = 3L;
        Long receiverId = 3L;
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        String target = "title";
        String keyword = "title";

        // when
        Page<Message> messages = workspaceRepository.findMessages(workspace, receiverId, target, keyword, pageable);

        // then
        assertEquals(2, messages.getContent().size());
        assertEquals(size, messages.getSize());
    }

    @DisplayName("쪽지 삭제")
    @Test
    void deleteMessage() {
        // given
        Long workspaceId = 3L;
        Long messageId = 1L;

        // when
        workspaceService.deleteMessage(workspaceId, messageId);

        // then
        assertThrows(MessageNotFoundException.class, () -> workspaceService.findMessage(workspaceId, messageId));
    }

    @DisplayName("쪽지 모두 읽기")
    @Test
    void readAllMessages() {
        // given
        Long workspaceId = 3L;
        Long receiverId = 3L;

        // when
        workspaceRepository.readAllMessages(workspaceId, receiverId);

        // then
        long count = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow().getMessages().stream()
                .filter(Message::isReaded)
                .count();
        assertEquals(2, count);
    }
}
