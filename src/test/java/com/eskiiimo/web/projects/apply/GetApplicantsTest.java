package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 지원자 리스트 확인하기")
public class GetApplicantsTest extends BaseControllerTest {

    @Test
    @DisplayName("프로젝트 지원자 확인하기_팀장일 때")
    @WithMockUser(username = "user0")
    void getApplicantsSuccess() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(2);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getApplicants",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("프로젝트 지원자 확인하기_권한없는 사용자")
    @WithMockUser(username = "authX")
    void getApplicantsFailBecause_noPermittedUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(107))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원자 확인하기_로그인하지 않은 사용자")
    void getApplicantsFailBecause_notLoginUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원자 확인하기_지원자가 없을때")
    @WithMockUser(username = "user0")
    void getApplicantsFailBecause_notExistApply() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(101))
                .andDo(print())
        ;
    }


}
