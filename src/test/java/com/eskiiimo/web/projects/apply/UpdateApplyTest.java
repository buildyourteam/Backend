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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 지원서 수정하기")
public class UpdateApplyTest extends BaseControllerTest {

    @Test
    @Transactional
    @DisplayName("내가 작성한 프로젝트 지원서 수정하기")
    @WithMockUser(username = "user1")
    void updateApply() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();
        projectApplyRequest.setIntroduction("지원서 수정 완료!");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("updateApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("answers").description("지원서 응답"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("introduction").description("자기소개")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(), "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("introduction").value("지원서 수정 완료!"))
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 수정하기_지원하지 않은 사용자")
    @WithMockUser(username = "user4")
    void updateApply_notApply() throws Exception {
        // Given
        testUserFactory.generateUser(4);
        Project project = testProjectFactory.generateProjectApplies(1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();
        projectApplyRequest.setIntroduction("지원서 수정 완료!");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

}
