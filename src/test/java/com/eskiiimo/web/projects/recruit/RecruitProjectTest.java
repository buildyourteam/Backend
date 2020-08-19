package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.request.RecruitProjectRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 영입하기")
public class RecruitProjectTest extends BaseControllerTest {
    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 영입하기")
    void recruitProjectSuccess() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user = testUserFactory.generateUser(1);
        RecruitProjectRequest recruitProjectRequest = testProjectFactory.generateRecruitRequest(project.getProjectId(), user);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", user.getUserId())
                .content(objectMapper.writeValueAsString(recruitProjectRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("projectRecruit",
                        pathParameters(
                                parameterWithName("userId").description("유저 아이디")
                        )
                ));
    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 영입하기_중복 영입지원")
    void recruitProjectFailBecause_duplicatedRecruit() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user = testUserFactory.generateUser(1);
        testProjectFactory.generateRecruit(user, project);
        RecruitProjectRequest recruitProjectRequest = testProjectFactory.generateRecruitRequest(project.getProjectId(), user);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", user.getUserId())
                .content(objectMapper.writeValueAsString(recruitProjectRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(109))
                .andDo(print())
                .andDo(document("109"))
        ;
    }

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("프로젝트 영입하기_권한없는 사용자")
    void recruitProjectFailBecause_noPermittedUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user = testUserFactory.generateUser(1);
        RecruitProjectRequest recruitProjectRequest = testProjectFactory.generateRecruitRequest(project.getProjectId(), user);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", user.getUserId())
                .content(objectMapper.writeValueAsString(recruitProjectRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(107))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 영입하기_로그인하지 않은 사용자")
    void recruitProjectFailBecause_notLoginUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user = testUserFactory.generateUser(1);
        RecruitProjectRequest recruitProjectRequest = testProjectFactory.generateRecruitRequest(project.getProjectId(), user);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", user.getUserId())
                .content(objectMapper.writeValueAsString(recruitProjectRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }
}
