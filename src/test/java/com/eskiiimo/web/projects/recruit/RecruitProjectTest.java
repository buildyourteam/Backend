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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecruitProjectTest extends BaseControllerTest {
    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 영입하기")
    void recruitProject() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user = testUserFactory.generateUser(1);
        RecruitProjectRequest recruitProjectRequest = testProjectFactory.generateRecruitRequest(project.getProjectId(), user);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", user.getUserId())
                .content(objectMapper.writeValueAsString(recruitProjectRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("projectRecruit",
                        pathParameters(
                                parameterWithName("userId").description("유저 아이디")
                        ),
                        requestFields(
                                fieldWithPath("projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("introduction").description("자기소개")
                        )
                ));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user1")
    @DisplayName("프로젝트 영입하기_권한없는 사용자")
    void recruitProject_authX() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user = testUserFactory.generateUser(1);
        RecruitProjectRequest recruitProjectRequest = testProjectFactory.generateRecruitRequest(project.getProjectId(), user);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", user.getUserId())
                .content(objectMapper.writeValueAsString(recruitProjectRequest))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }
}
