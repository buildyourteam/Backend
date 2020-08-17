package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("나한테 온 프로젝트 영입 제안리스트")
public class GetRecruitsToMeTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트")
    void getRecruitListToMeSuccess() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", userId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getRecruits",
                        pathParameters(
                                parameterWithName("userId").description("유저 아이디")
                        )
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트_권한 없는 사용자")
    void getRecruitListToMeFailBecause_noPermittedUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", userId))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(104))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("나한테 온 프로젝트 영입 제안리스트_로그인하지 않은 사용자")
    void getRecruitListToMeFailBecause_notLoginUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", userId))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트_영입제안이 없을 때")
    void getRecruitListToMeFailBecause_notExistRecruit() throws Exception {

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", "user0"))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }
}
