package com.eskiiimo.web.security;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.security.provider.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("JWT 필터 테스트")
class JwtFilterTest extends BaseControllerTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    @Transactional
    @DisplayName("Bearer 방식의 인증이 아닐 때")
    void FailBecauseNotBearer() throws Exception {
        testUserFactory.generateUser(1);
        String token = jwtTokenProvider.createAccessToken("TestUser1", Collections.singletonList("ROLE_USER"));
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects")
                .header("Authorization", token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value("006"))
                .andDo(document("006"))
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("만료된 토큰 일 때")
    void FailBecauseExpired() throws Exception {
       testUserFactory.generateUser(1);
        String token = jwtTokenProvider.generateToken("TestUser1", Collections.singletonList("ROLE_USER"),-10);
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects")
                .header("Authorization", "Bearer "+token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value("007"))
                .andDo(document("007"))
                .andDo(print())
        ;
    }
}