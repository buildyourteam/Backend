package com.eskiiimo.web.projects.detail;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 수정하기")
public class UpdateProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 수정하기_팀장일 때")
    public void updateProjectForLeaderSuccess() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest projectDetailRequest = testProjectFactory.generateProjectUpdateRequest(myProject);
        projectDetailRequest.setProjectName("Hi updated project....");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}", myProject.getProjectId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(projectDetailRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-project"))
        ;
    }

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("프로젝트 수정하기_팀장이 아닐 때")
    public void updateProjectFailBecause_notLeader() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest projectDetailRequest = testProjectFactory.generateProjectUpdateRequest(myProject);
        projectDetailRequest.setProjectName("Hi updated project....");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}", myProject.getProjectId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(projectDetailRequest)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(107))
        ;
    }

    @Test
    @DisplayName("프로젝트 수정하기_로그인하지 않은 사용지")
    public void updateProjectFailBecause_notLoginUser() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest projectDetailRequest = testProjectFactory.generateProjectUpdateRequest(myProject);
        projectDetailRequest.setProjectName("Hi updated project....");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}", myProject.getProjectId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(projectDetailRequest)))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}
