package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("나한테 온 프로젝트 영입 제안리스트")
public class GetRecruitsToMeTest extends BaseControllerTest {

    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트")
    void getRecruitListToMe() throws Exception {
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
                        ),
                        responseFields(
                                fieldWithPath("_embedded.recruitDtoList[].userName").description("유저이름"),
                                fieldWithPath("_embedded.recruitDtoList[].introduction").description("자기소개"),
                                fieldWithPath("_embedded.recruitDtoList[].role").description("지원할 역할"),
                                fieldWithPath("_embedded.recruitDtoList[].state").description("상태"),
                                fieldWithPath("_embedded.recruitDtoList[].projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("_embedded.recruitDtoList[].projectName").description("영입 제안 프로젝트 이름"),
                                fieldWithPath("_embedded.recruitDtoList[]._links.self.href").description("self 링크"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user1")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트_권한 없는 사용자")
    void getRecruitListToMe_authX() throws Exception {
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
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트_권한 없는 사용자2")
    void getRecruitListToMe_authX2() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", "authX"))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트_영입제안이 없을 때")
    void getRecruitListToMe_notExist() throws Exception {

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", "user0"))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }
}
