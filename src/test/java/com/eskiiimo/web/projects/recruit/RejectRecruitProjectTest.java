package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("영입제안 거절하기")
public class RejectRecruitProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("영입제안 거절하기")
    void rejectRecruitProjectSuccess() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("rejectRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;

        this.mockMvc.perform(get("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("REJECT"));
    }

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("영입제안 거절하기_권한 없는 사용자")
    void rejectRecruitProjectFailBecause__noAuthUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(104))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("영입제안 거절하기_로그인하지 않은 사용자")
    void rejectRecruitProjectFailBecause__notLoginUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("영입제안 거절하기_영입제안이 없을 때")
    void rejectRecruitProjectFailBecause_notExistRecruit() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/recruit/{projectId}", project.getLeaderId(), project.getProjectId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(105))
                .andDo(print())
        ;
    }
}
