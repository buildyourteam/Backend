package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AcceptMemberTest extends BaseControllerTest {
    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 수락하기_팀장일 때")
    @WithMockUser(username = "user0")
    void acceptMember_leader() throws Exception {
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
        this.mockMvc.perform(get("/projects/{projectId}", project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("memberList[1].userName").value("UserName4"))
                .andExpect(jsonPath("memberList[1].role").value("DEVELOPER"))
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 수락하기_팀장이 아닐 때")
    @WithMockUser(username = "user1")
    void acceptMember_notLeader() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isForbidden())

        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 수락하기_지원자가 없을 때")
    @WithMockUser(username = "user0")
    void acceptMember_notExistApply() throws Exception {
        // Given
        testUserFactory.generateUser(2);
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user2"))
                .andExpect(status().isNotFound())

        ;
    }
}
