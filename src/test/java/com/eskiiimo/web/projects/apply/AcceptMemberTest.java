package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 지원자 수락하기")
public class AcceptMemberTest extends BaseControllerTest {
    @Test
    @DisplayName("프로젝트 지원자 수락하기_팀장일 때")
    @WithMockUser(username = "user0")
    void acceptMemberSuccess() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("acceptApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("ACCEPT"))
        ;
    }

    @Test
    @DisplayName("프로젝트 지원자 수락하기_팀장이 아닐 때")
    @WithMockUser(username = "user1")
    void acceptMemberFailBecause_notLeader() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(107))
                .andDo(print())
        .andDo(document("107"))
        ;
    }

    @Test
    @DisplayName("프로젝트 지원자 수락하기_로그인하지 않은 사용자")
    void acceptMemberFailBecause_notLoginUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원자 수락하기_지원자가 없을 때")
    @WithMockUser(username = "user0")
    void acceptMemberFailBecause_notExistApply() throws Exception {
        // Given
        testUserFactory.generateUser(2);
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(102))
                .andDo(print())
                .andDo(document("102"))
        ;
    }
}
