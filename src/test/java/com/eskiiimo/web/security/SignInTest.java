package com.eskiiimo.web.security;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.security.request.SignInRequest;
import com.eskiiimo.web.security.request.SignUpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그인 테스트")
class SignInTest extends BaseControllerTest {

    @Test
    @DisplayName("로그인")
    void signInSuccess() throws Exception {

        User user = this.testUserFactory.generateUser(1);

        SignInRequest signInRequest = SignInRequest.builder()
                .userId(user.getUserId())
                .password("password")
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
    @DisplayName("로그인_없는 사용자")
    void signInFailBecauseNotExist() throws Exception {

        SignInRequest signInRequest = SignInRequest.builder()
                .userId("user1")
                .password("testPassword")
                .build();

        this.mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value("002"))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("로그인_정보가 잘못되었을 때")
    void signInFailBecauseWrongIDorPW() throws Exception {

        User user = this.testUserFactory.generateUser(1);

        SignInRequest signInRequest = SignInRequest.builder()
                .userId("user1")
                .password("testPasswordd")
                .build();

        this.mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value("002"))
                .andDo(document("002"))
                .andDo(print())
        ;
    }
}