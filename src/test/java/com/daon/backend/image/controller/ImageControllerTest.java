package com.daon.backend.image.controller;

import com.daon.backend.config.S3MockConfig;
import com.daon.backend.image.infrastructure.S3ImageFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@Slf4j
@AutoConfigureMockMvc
@Import({
        S3MockConfig.class,
        S3ImageFileService.class
})
@WebMvcTest(controllers = ImageController.class)
public class ImageControllerTest {

    @Autowired
    MockMvc mockMvc;

    @WithMockUser(roles = "MEMBER")
    @Test
    @DisplayName("이미지 업로드 테스트")
    void imageUploadTest() throws Exception {
        //given
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",
                "test-img.png",
                ContentType.IMAGE_PNG.getMimeType(),
                new FileInputStream(ResourceUtils.getFile(this.getClass().getResource("/static/test-img.png")))
        );

        //when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.multipart("/api/images")
                        .file(mockMultipartFile)
                        .header(HttpHeaders.AUTHORIZATION, "any-user-id")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .with(csrf())
        );

        //then
        perform.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.imageUrl").isNotEmpty());
    }
}
