package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.request.ProjectApplyRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 지원서 수정하기")
public class UpdateApplyTest extends BaseControllerTest {

    @Test
    @DisplayName("내가 작성한 프로젝트 지원서 수정하기")
    @WithMockUser(username = "user1")
    void updateApplySuccess() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();
        projectApplyRequest.setIntroduction("지원서 수정 완료!");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("updateApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("introduction").value("지원서 수정 완료!"))
        ;
    }

    @Test
    @DisplayName("내가 작성한 프로젝트 지원서 수정하기_로그인하지 않은 사용자")
    void updateApplySuccess_notLoginUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();
        projectApplyRequest.setIntroduction("지원서 수정 완료!");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원서 수정하기_지원하지 않은 사용자")
    @WithMockUser(username = "user4")
    void updateApplyFailBecause_notApply() throws Exception {
        // Given
        testUserFactory.generateUser(4);
        Project project = testProjectFactory.generateProjectApplies(1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();
        projectApplyRequest.setIntroduction("지원서 수정 완료!");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(102))
                .andDo(print())
        ;
    }

}
