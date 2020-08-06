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

@DisplayName("영입제안 승락하기")
public class AcceptRecruitProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("영입제안 승락하기")
    void acceptRecruitProjectSuccess() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("acceptRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ));

        this.mockMvc.perform(get("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("ACCEPT"))
        ;
    }

    @Test
    @WithMockUser(username = "authX")
    @DisplayName("영입제안 승락하기_권한 없는 사용자")
    void acceptRecruitProjectFailBecause_noAuthUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(104))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("영입제안 승락하기_로그인하지 않은 사용자")
    void acceptRecruitProjectFailBecause_notLoginUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }


    @Test
    @WithMockUser(username = "user0")
    @DisplayName("영입제안 승락하기_존재하지 않는 제안서")
    void acceptRecruitProjectFailBecause_notExistRecruit() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        String userId = project.getLeaderId();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/recruit/{projectId}", userId, project.getProjectId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(105))
                .andDo(print())
        ;
    }


}
