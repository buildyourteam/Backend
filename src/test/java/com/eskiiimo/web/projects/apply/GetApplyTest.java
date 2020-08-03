package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
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

@DisplayName("프로젝트 지원서 한개 확인하기")
public class GetApplyTest extends BaseControllerTest {

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 한개 확인하기_팀장일때")
    @WithMockUser(username = "user0")
    void getApply_leader() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("state").description("상태"),
                                fieldWithPath("questions").description("질문"),
                                fieldWithPath("answers").description("응답"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.acceptApply.href").description("지원서 승인하기"),
                                fieldWithPath("_links.rejectApply.href").description("지원서 거절하기"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("acceptApply").description("지원서 승인하기"),
                                linkWithRel("rejectApply").description("지원서 거절하기"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;

    }

    @Test
    @Transactional
    @DisplayName("내가 작성한 프로젝트 지원서 한개 확인하기_팀장이 아닐때")
    @WithMockUser(username = "user1")
    void getApply_member() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 한개 확인하기_권한이 없는 사용자")
    @WithMockUser(username = "user4")
    void getApply_notMember() throws Exception {
        // Given
        testUserFactory.generateUser(4);
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 한개 확인하기_없는 지원서일 때")
    @WithMockUser(username = "user0")
    void getApply_notExistApply() throws Exception {
        // Given
        testUserFactory.generateUser(4);
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user2"))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 한개 확인하기_없는 프로젝트일 때")
    @WithMockUser(username = "user0")
    void getApply_notExistProject() throws Exception {
        // Given
        testUserFactory.generateUser(2);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", (long)1, "user2"))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

}
