package com.eskiiimo.web.security.controller;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.security.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원가입 테스트")
class SignUpTest extends BaseControllerTest {

    @Test
    @DisplayName("회원가입")
    void SignUpSuccess() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("testid")
                .userEmail("tester@eskiiimo.com")
                .name("testUser")
                .password("testPassword")
                .build();

        this.mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("signup",
                        requestFields(
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("userEmail").description("이메일주소")
                        )
                ))
        ;
    }
}