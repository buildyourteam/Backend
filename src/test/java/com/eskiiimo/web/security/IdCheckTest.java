package com.eskiiimo.web.security;

import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("아이디 중복체크")
class IdCheckTest extends BaseControllerTest {

    @Test
    @DisplayName("아이디 중복체크")
    @WithMockUser(username = "tester")
    void idCheckSuccess() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(post("/auth/idcheck/{checkId}", "test"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("idCheck"))
        ;
    }

    @Test
    @DisplayName("아이디 중복체크_중복일때")
    @WithMockUser(username = "tester")
    void idCheckFailBecause_duplicatedID() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(post("/auth/idcheck/{checkId}", "user1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("error").value(3))
                .andDo(print())
                .andDo(document("003"))
        ;
    }
}