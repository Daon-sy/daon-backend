package com.daon.backend.task.service;

import com.daon.backend.config.MockConfig;
import com.daon.backend.member.domain.Member;
import com.daon.backend.member.domain.MemberRepository;
import com.daon.backend.member.domain.PasswordEncoder;
import com.daon.backend.security.MemberPrincipal;
import com.daon.backend.task.domain.workspace.*;
import com.daon.backend.task.dto.workspace.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.workspace.InviteMemberRequestDto;
import com.daon.backend.task.dto.workspace.JoinWorkspaceRequestDto;
import com.daon.backend.task.dto.workspace.SendMessageRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
@SpringBootTest
public class MessageServiceTest extends MockConfig {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    EntityManager em;

    private Long workspaceId;

    private Long receiverId;

    private String testMemberId;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .username("user")
                .password("1234")
                .name("유저")
                .email("user@email.com")
                .passwordEncoder(passwordEncoder)
                .build();

        String savedMemberId = memberRepository.save(member).getId();
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        new MemberPrincipal(savedMemberId),
                        null
                )
        );

        CreateWorkspaceRequestDto requestDto = new CreateWorkspaceRequestDto(
                new CreateWorkspaceRequestDto.WorkspaceInfo(
                        "워크스페이스 제목",
                        null,
                        "워크스페이스 설명",
                        "워크스페이스 주제"
                ),
                new CreateWorkspaceRequestDto.WorkspaceProfileInfo(
                        "홍길동",
                        null,
                        "user@email.com"
                )
        );

        workspaceId = workspaceService.createWorkspace(requestDto).getWorkspaceId();
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();

        Member testMember = Member.builder()
                .username("test user")
                .password("1234")
                .name("테스트 이름")
                .email("testUser@email.com")
                .passwordEncoder(passwordEncoder)
                .build();
        testMemberId = memberRepository.save(testMember).getId();

        InviteMemberRequestDto request = new InviteMemberRequestDto("test user", Role.PROJECT_ADMIN);
        workspaceService.inviteMember(workspaceId, request);

        JoinWorkspaceRequestDto joinWorkspaceRequestDto = new JoinWorkspaceRequestDto(
                "홍길동",
                null,
                "user2@email.com"
        );
        workspace.addParticipant(
                testMemberId,
                new Profile(
                        joinWorkspaceRequestDto.getName(),
                        joinWorkspaceRequestDto.getImageUrl(),
                        joinWorkspaceRequestDto.getEmail()
                )
        );
        em.flush();
        em.clear();

        WorkspaceParticipant receiver = workspace.findWorkspaceParticipantByMemberId(testMemberId);
        receiverId = receiver.getId();

        SendMessageRequestDto sendMessageRequestDto = new SendMessageRequestDto(
                "쪽지 제목",
                "쪽지 내용",
                receiverId
        );
        workspaceService.createMessage(workspaceId, sendMessageRequestDto);
        em.flush();
        em.clear();
    }

    @DisplayName("쪽지 보내기")
    @Test
    void createMessage() {
        // given
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();
        Long messageId = workspace.getMessages().get(0).getId();

        // when
        Message message = workspace.findMessage(messageId, receiverId);

        // then
        assertEquals(messageId, message.getId());
        assertEquals("쪽지 제목", message.getTitle());
        assertEquals("쪽지 내용", message.getContent());
    }

    @DisplayName("쪽지 단건 조회")
    @Test
    void findMessage() {
        // given
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();
        Long messageId = workspace.getMessages().get(0).getId();
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(testMemberId).getId();

        // when
        Message message = workspace.findMessage(messageId, receiverId);

        // then
        assertEquals(messageId, message.getId());
        assertEquals("쪽지 제목", message.getTitle());
        assertEquals("쪽지 내용", message.getContent());
    }

    @DisplayName("쪽지 목록 조회")
    @Test
    void findMessages() {
        // given
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(testMemberId).getId();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        String target = "title";
        String keyword = "제목";

        // when
        Page<Message> messages = workspaceRepository.findMessages(workspace, receiverId, target, keyword, pageable);

        // then
        assertEquals(size, messages.getSize());
        assertEquals(1, messages.getContent().size());
    }

    @DisplayName("쪽지 삭제")
    @Test
    void deleteMessage() {
        // given
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(testMemberId).getId();
        Message findMessage = workspace.getMessages().stream()
                .filter(message -> message.getReceiverId().equals(receiverId))
                .findFirst()
                .orElseThrow();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        String target = "title";
        String keyword = "제목";

        // when
        workspace.deleteMessage(findMessage.getId(), receiverId);

        // then
        Page<Message> messages = workspaceRepository.findMessages(workspace, receiverId, target, keyword, pageable);
        assertEquals(0, messages.getContent().size());
    }

    @DisplayName("쪽지 모두 읽기")
    @Test
    void readAllMessages() {
        // given
        Workspace workspace = workspaceRepository.findWorkspaceById(workspaceId).orElseThrow();
        Long receiverId = workspace.findWorkspaceParticipantByMemberId(testMemberId).getId();
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        String target = "title";
        String keyword = "제목";

        // when
        workspaceRepository.readAllMessages(workspaceId, receiverId);

        // then
        Page<Message> messages = workspaceRepository.findMessages(workspace, receiverId, target, keyword, pageable);
        assertTrue(messages.getContent().get(0).isReaded());
    }
}
