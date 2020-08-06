package com.eskiiimo.web.user.profile;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.State;
import org.junit.jupiter.api.Disabled;
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

@DisplayName("숨긴 프로젝트 취소하기")
@Disabled
public class ReShowProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("숨긴 프로젝트 취소하기")
    public void reShowProjectSuccess() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RUNNING);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project5, Boolean.TRUE);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("reshowProject",
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디"),
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )));
    }

    @Test
    @WithMockUser(username = "user2")
    @DisplayName("숨긴 프로젝트 취소하기_권한 없는 사용자")
    public void reShowProjectFailBecause_noPermittedUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RUNNING);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project5, Boolean.TRUE);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(201))
        ;
    }

    @Test
    @DisplayName("숨긴 프로젝트 취소하기_로그인하지 않은 사용자")
    public void reShowProjectFailBecause_notLgoinUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RUNNING);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project5, Boolean.TRUE);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}
