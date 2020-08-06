package com.eskiiimo.web.user.profile;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 숨기기")
public class HideProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("프로젝트 숨기기")
    public void hideProjectSuccess() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RUNNING);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project5, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("hideProject",
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디"),
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )))

        ;

    }


    @Test
    @WithMockUser(username = "user2")
    @DisplayName("프로젝트 숨기기_권한없는 사용자")
    public void hideProjectFailBecause_noPermittedUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        Project project4 = testProjectFactory.generateProject(4, user1, State.RUNNING);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(201))
        ;
    }

    @Test
    @DisplayName("프로젝트 숨기기_로그인하지 않은 사용자")
    public void hideProjectFailBecause_notLoginUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        Project project4 = testProjectFactory.generateProject(4, user1, State.RUNNING);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

}
