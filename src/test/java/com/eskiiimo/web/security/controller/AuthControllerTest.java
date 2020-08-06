package com.eskiiimo.web.security.controller;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.security.request.RefreshRequest;
import com.eskiiimo.web.security.request.SignInRequest;
import com.eskiiimo.web.security.request.SignUpRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증")
class AuthControllerTest extends BaseControllerTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("로그인")
    void signInSuccess() throws Exception {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("user1")
                .userEmail("tester@eskiiimo.com")
                .name("testUser")
                .password("testPassword")
                .build();
        this.mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signUpRequest)));

        SignInRequest signInRequest = SignInRequest.builder()
                .userId("user1")
                .password("testPassword")
                .build();

        this.mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accessToken").exists())
                .andExpect(jsonPath("refreshToken").exists())
                .andDo(print())
                .andDo(document("signin",
                        requestFields(
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("password").description("비밀번호")
                        )
                ))
        ;

    }

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

    @Test
    @DisplayName("아이디 중복체크_중복일때")
    @WithMockUser(username = "tester")
    void icCheckFailBecause_duplicatedID() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(post("/auth/idcheck/{checkId}", "user1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("error").value(3))
                .andDo(print())
                .andDo(document("idCheck"))
        ;
    }

    @Test
    @DisplayName("아이디 중복체크_사용가능")
    @WithMockUser(username = "tester")
    void icCheckSuccess() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(post("/auth/idcheck/{checkId}", "test"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("idCheckOk"))
        ;
    }

    @Test
    @Transactional
    @DisplayName("토큰 재발급 받기(성공)")
    void refreshTokenSuccess() throws Exception {
        User user = testUserFactory.generateUser(1);
        RefreshRequest refreshRequest = RefreshRequest.builder()
                .refreshToken(user.getRefreshToken())
                .build();
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("refresh"));
    }

    @Test
    @Disabled       // Admin Api 개발 이후 활성화 필요
    @Transactional
    @DisplayName("토큰 재발급 받기(계정이 제제당했을 때)")
    void refreshTokenFailBecauseDifferentUserToken() throws Exception {
        User user = testUserFactory.generateUser(1);
        // 계정 제제 로직 필요
        RefreshRequest refreshRequest = RefreshRequest.builder()
                .refreshToken(user.getRefreshToken())
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("토큰 재발급 받기(서명이 일치하지 않았을 때)")
    void refreshTokenFailBecauseSignature() throws Exception {
        User user = testUserFactory.generateUser(1);
        RefreshRequest refreshRequest = RefreshRequest.builder()
                .refreshToken(user.getRefreshToken() + "e")
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(4))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("토큰 재발급 받기(토큰이 깨졌을 때 실패)")
    void refreshTokenFailBecauseMalFormed() throws Exception {
        User user = testUserFactory.generateUser(1);
        RefreshRequest refreshRequest = RefreshRequest.builder()
                .refreshToken("e" + user.getRefreshToken())
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(5))
                .andDo(print());
    }

}