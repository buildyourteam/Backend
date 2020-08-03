package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
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

@DisplayName("프로젝트 지원하기")
public class ApplyProjectTest extends BaseControllerTest {

    @Test
    @Transactional
    @DisplayName("프로젝트 지원하고 내 지원서 확인하기")
    @WithMockUser(username = "user1")
    void applyProject() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User me = testUserFactory.generateUser(1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("applyProject",
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
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(), me.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userName").value("user1"))
                .andExpect(jsonPath("role").value("DEVELOPER"))
                .andExpect(jsonPath("introduction").value("안녕하세요? 저는 그냥 개발자입니다."))
                .andExpect(jsonPath("answers[0]").value("1번 응답"))
                .andExpect(jsonPath("answers[1]").value("2번 응답"))
                .andExpect(jsonPath("answers[2]").value("3번 응답"))
        ;

    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원하기_비회원 일 때")
    @WithMockUser(username = "user1")
    void applyProject_notUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;

    }
}
