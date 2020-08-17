package com.eskiiimo.web.security;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.security.request.RefreshRequest;
import com.eskiiimo.web.user.enumtype.UserActivate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Access Token 재발급")
class RefreshTokenTest extends BaseControllerTest {

    @Test
    @Transactional
    @DisplayName("토큰 재발급 받기")
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
    @Transactional
    @DisplayName("토큰 재발급 받기_제제당하거나 없는 계정")
    void refreshTokenFailBecauseNotActive() throws Exception {
        User user = testUserFactory.generateUser(1, UserActivate.BLOCKED);
        // 계정 제제 로직 필요
        RefreshRequest refreshRequest = RefreshRequest.builder()
                .refreshToken(user.getRefreshToken())
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(1))
                .andDo(document("001"))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("토큰 재발급 받기_서명이 다른 토큰")
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
                .andDo(document("004"))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("토큰 재발급 받기_해석불가능한 토큰")
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
                .andDo(document("005"))
                .andDo(print());
    }

}