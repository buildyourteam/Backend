package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 지원서 한개 확인하기")
public class GetApplyTest extends BaseControllerTest {

    @Test
    @DisplayName("프로젝트 지원서 한개 확인하기_팀장일때")
    @WithMockUser(username = "user0")
    void getApplyForLeaderSuccess() throws Exception {
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
    @DisplayName("내가 작성한 프로젝트 지원서 한개 확인하기_팀장이 아닐때")
    @WithMockUser(username = "user1")
    void getApplyForMemberSuccess() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getMyApply",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("updateApply").description("지원서 수정하기"),
                                linkWithRel("profile").description("Api 명세서")
                        )))
        ;
    }

    @Test
    @DisplayName("프로젝트 지원서 한개 확인하기_권한이 없는 사용자")
    @WithMockUser(username = "user4")
    void getApplyFailBecause_noPermittedMember() throws Exception {
        // Given
        testUserFactory.generateUser(4);
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(107))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원서 한개 확인하기_로그인하지 않은 사용자")
    void getApplyFailBecause_notLoginUser() throws Exception {
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
    @DisplayName("프로젝트 지원서 한개 확인하기_없는 지원서일 때")
    @WithMockUser(username = "user0")
    void getApplyFailBecause_notExistApply() throws Exception {
        // Given
        testUserFactory.generateUser(4);
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(102))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원서 한개 확인하기_없는 프로젝트일 때")
    @WithMockUser(username = "user0")
    void getApplyFailBecause_notExistProject() throws Exception {
        // Given
        testUserFactory.generateUser(2);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", (long) 1, "user2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(103))
                .andDo(print())
                .andDo(document("103"))
        ;
    }

}
