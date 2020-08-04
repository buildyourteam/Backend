package com.eskiiimo.web.projects.detail;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 삭제하기")
public class DeleteProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user0")
    @Transactional
    @DisplayName("프로젝트 삭제하기_팀장일때")
    public void deleteProject_leader() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{project_id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-project",
                        pathParameters(
                                parameterWithName("project_id").description("Project id")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        )
                ));
    }

    @Test
    @WithMockUser(username = "user1")
    @Transactional
    @DisplayName("프로젝트 삭제하기_팀장이 아닐때")
    public void deleteProject_notLeader() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{project_id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @WithMockUser(username = "user1")
    @Transactional
    @DisplayName("프로젝트 삭제하기_존재하지 않는 프로젝트일 때")
    public void deleteProject_notExist() throws Exception {
        // Given

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{project_id}", (long) 1)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }
}
