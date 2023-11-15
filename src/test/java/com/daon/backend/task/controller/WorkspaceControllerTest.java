package com.daon.backend.task.controller;

import com.daon.backend.task.domain.workspace.Role;
import com.daon.backend.task.domain.workspace.Workspace;
import com.daon.backend.task.domain.workspace.WorkspaceCreator;
import com.daon.backend.task.dto.workspace.CreateWorkspaceRequestDto;
import com.daon.backend.task.dto.workspace.InviteMemberRequestDto;
import com.daon.backend.task.dto.workspace.FindWorkspaceParticipantsResponseDto;
import com.daon.backend.task.dto.workspace.FindProfileResponseDto;
import com.daon.backend.task.dto.workspace.WorkspaceListResponseDto;
import com.daon.backend.task.infrastructure.CheckRoleInterceptor;
import com.daon.backend.task.service.WorkspaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class WorkspaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private WorkspaceService workspaceService;

    @MockBean
    private CheckRoleInterceptor interceptor;

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        when(interceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @DisplayName("createWorkspace(): 워크스페이스 생성")
    @Test
    void createWorkspace() throws Exception {
        // given
        final String url = "/api/workspaces";
        final Long workspaceId = 1L;

        CreateWorkspaceRequestDto.WorkspaceInfo workspaceInfo =
                CreateWorkspaceRequestDto.WorkspaceInfo.builder()
                        .title("테스트 워크스페이스")
                        .imageUrl("https://xxx.xxxx.xxxx")
                        .subject("테스트")
                        .description("테스트 용도로 생성")
                        .build();
        CreateWorkspaceRequestDto.WorkspaceProfileInfo workspaceProfileInfo =
                CreateWorkspaceRequestDto.WorkspaceProfileInfo.builder()
                        .name("홍길동")
                        .imageUrl("https://xxx.xxxx.xxxx")
                        .email("test@gmail.com")
                        .build();
        CreateWorkspaceRequestDto requestDto = new CreateWorkspaceRequestDto(workspaceInfo, workspaceProfileInfo);

        given(workspaceService.createWorkspace(Mockito.any(CreateWorkspaceRequestDto.class)))
                .willReturn(workspaceId);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.workspaceId").value(workspaceId));
    }

    @DisplayName("workspaceList(): 워크스페이스 목록 조회")
    @Test
    void workspaceList() throws Exception {
        // given
        final String url = "/api/workspaces";
        final String memberId = "uuid";

        WorkspaceCreator creator = WorkspaceCreator.builder()
                .memberId(memberId)
                .profileName("홍길동")
                .build();

        Workspace workspace1 = Workspace.createOfPersonal(creator);
        Workspace workspace2 = Workspace.createOfGroup(
                "테스트 워크스페이스",
                "테스트용 워크스페이스입니다",
                "https://xxx.xxxx.xxxx",
                "테스트",
                creator
        );

        List<WorkspaceListResponseDto.WorkspaceSummary> workspaceList =
                Arrays.asList(
                        new WorkspaceListResponseDto.WorkspaceSummary(workspace1),
                        new WorkspaceListResponseDto.WorkspaceSummary(workspace2)
                );

        WorkspaceListResponseDto responseDto = new WorkspaceListResponseDto(workspaceList);

        given(workspaceService.findAllWorkspace())
                .willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(url)
                .contentType(APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCount").value(responseDto.getWorkspaces().size()))
                .andExpect(jsonPath("$.data.workspaces[0].title").value(responseDto.getWorkspaces().get(0).getTitle()))
                .andExpect(jsonPath("$.data.workspaces[0].division").value(responseDto.getWorkspaces().get(0).getDivision()))
                .andExpect(jsonPath("$.data.workspaces[1].title").value(responseDto.getWorkspaces().get(1).getTitle()))
                .andExpect(jsonPath("$.data.workspaces[1].imageUrl").value(responseDto.getWorkspaces().get(1).getImageUrl()))
                .andExpect(jsonPath("$.data.workspaces[1].division").value(responseDto.getWorkspaces().get(1).getDivision()));
    }

    @DisplayName("checkJoinCode(): 참여코드 확인")
    @Test
    void checkJoinCode() throws Exception {
        // given
        final String url = "/api/workspaces/code";
        final String joinCode = "joinCode";

        CheckJoinCodeRequestDto requestDto = new CheckJoinCodeRequestDto(joinCode);
        doNothing().when(workspaceService).checkJoinCode(requestDto);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody)
        );

        // then
        result.andExpect(status().isOk());
    }

    @DisplayName("joinWorkspace(): 워크스페이스 참여")
    @Test
    void joinWorkspace() throws Exception {
        // given
        final String url = "/api/workspaces/join";
        final String joinCode = "joinCode";
        final Long workspaceId = 1L;

        JoinWorkspaceRequestDto requestDto = new JoinWorkspaceRequestDto(
                joinCode,
                new JoinWorkspaceRequestDto.WorkspaceProfileInfo(
                        "홍길동",
                        "https://xxx.xxxx.xxxx",
                        "test@gmail.com"
                )
        );
        JoinWorkspaceResponseDto responseDto = new JoinWorkspaceResponseDto(workspaceId);
        given(workspaceService.joinWorkspace(Mockito.any(JoinWorkspaceRequestDto.class)))
                .willReturn(responseDto);

        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody)
        );

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.workspaceId").value(workspaceId));


    }

    @DisplayName("findProfile(): 프로필 조회")
    @Test
    void findProfile() throws Exception {
        final String url = "/api/workspaces/{workspaceId}/profile/me";
        final Long workspaceId = 1L;

        FindProfileResponseDto responseDto = new FindProfileResponseDto(
                1L,
                "홍길동",
                "https://xxx.xxxx.xxxx",
                "test@gmail.com",
                Role.BASIC_PARTICIPANT
        );

        given(workspaceService.findProfile(Mockito.anyLong()))
                .willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(url, workspaceId)
                .contentType(APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.participantId").value(responseDto.getWorkspaceParticipantId()))
                .andExpect(jsonPath("$.data.name").value(responseDto.getName()))
                .andExpect(jsonPath("$.data.imageUrl").value(responseDto.getImageUrl()))
                .andExpect(jsonPath("$.data.email").value(responseDto.getEmail()))
                .andExpect(jsonPath("$.data.role").value(responseDto.getRole().name()));
    }

    @DisplayName("findParticipants(): 워크스페이스 구성원 목록 조회")
    @Test
    void findParticipants() throws Exception {
        // given
        final String url = "/api/workspaces/{workspaceId}/participants";
        final Long workspaceId = 1L;

        FindWorkspaceParticipantsResponseDto responseDto = new FindWorkspaceParticipantsResponseDto(
                List.of(
                        new FindWorkspaceParticipantsResponseDto.WorkspaceParticipantProfile(
                                workspaceId,
                                "홍길동",
                                "test@gmail.com",
                                "https://xxx.xxxx.xxxx",
                                Role.BASIC_PARTICIPANT
                        )
                )
        );

        given(workspaceService.findWorkspaceParticipants(workspaceId))
                .willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(url, workspaceId)
                .contentType(APPLICATION_JSON_VALUE));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCount").value(responseDto.getTotalCount()))
                .andExpect(jsonPath("$.data.participants[0].participantId").value(responseDto.getWorkspaceParticipants().get(0).getWorkspaceParticipantId()))
                .andExpect(jsonPath("$.data.participants[0].name").value(responseDto.getWorkspaceParticipants().get(0).getName()))
                .andExpect(jsonPath("$.data.participants[0].imageUrl").value(responseDto.getWorkspaceParticipants().get(0).getImageUrl()))
                .andExpect(jsonPath("$.data.participants[0].email").value(responseDto.getWorkspaceParticipants().get(0).getEmail()))
                .andExpect(jsonPath("$.data.participants[0].role").value(responseDto.getWorkspaceParticipants().get(0).getRole().name()));
    }

    @DisplayName("inviteMember(): 회원 초대")
    @Test
    void inviteMember() throws Exception {
        // given
        final String url = "/api/workspaces/{workspaceId}/invite";
        final Long workspaceId = 1L;
        final String email = "test@gmail.com";

        InviteMemberRequestDto requestDto = new InviteMemberRequestDto(email);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        // when
        ResultActions result = mockMvc.perform(post(url, workspaceId)
                .contentType(APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isOk());
    }
}