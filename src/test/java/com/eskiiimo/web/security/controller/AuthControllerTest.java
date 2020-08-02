package com.eskiiimo.web.security.controller;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.security.request.SignInRequest;
import com.eskiiimo.web.security.request.SignUpRequest;
import com.eskiiimo.repository.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증")
class AuthControllerTest extends BaseControllerTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("로그인")
    @Transactional
    void signin() throws Exception {

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .userId("user1")
                .userEmail("tester@eskiiimo.com")
                .name("testUser")
                .password("testPassword")
                .build();
        this.mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signUpRequest)));
//
                SignInRequest signInRequest = SignInRequest.builder()
                .userId("user1")
                .password("testPassword")
                .build();

        this.mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(header().exists("authToken"))
                .andDo(print())
                .andDo(document("signin",
                        requestFields(
                                fieldWithPath("userId").description("아이디"),
                                fieldWithPath("password").description("비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("authToken").description("로그인 토큰")
                        )
                ))
        ;

    }

    @Test
    @DisplayName("로그아웃")
    @Transactional
    void Signup() throws Exception {
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
    @Transactional
    @DisplayName("아이디 중복체크_중복일때")
    @WithMockUser(username = "tester")
    void icCheckNo() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(post("/auth/idcheck/{checkId}","user1"))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andDo(document("idCheck"))
        ;
    }

    @Test
    @DisplayName("아이디 중복체크_사용가능")
    @WithMockUser(username = "tester")
    void icCheckOk() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(post("/auth/idcheck/{checkId}","test"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("idCheckOk"))
        ;
    }
}