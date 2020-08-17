package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.State;
import com.eskiiimo.web.projects.request.ProjectApplyRequest;
import com.eskiiimo.web.user.enumtype.UserActivate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 지원하기")
public class ApplyProjectTest extends BaseControllerTest {

    @Test
    @DisplayName("프로젝트 지원하고 내 지원서 확인하기")
    @WithMockUser(username = "user1")
    void applyProjectSuccess() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User me = testUserFactory.generateUser(1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("applyProject",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(), me.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userName").value(me.getUserName()))
                .andExpect(jsonPath("role").value("DEVELOPER"))
                .andExpect(jsonPath("introduction").value("안녕하세요? 저는 그냥 개발자입니다."))
                .andExpect(jsonPath("answers[0]").value("1번 응답"))
                .andExpect(jsonPath("answers[1]").value("2번 응답"))
                .andExpect(jsonPath("answers[2]").value("3번 응답"))
        ;

    }

    @Test
    @DisplayName("프로젝트 지원_프로젝트 리더가 지원")
    @WithMockUser(username = "user1")
    void applyProjectFailBecause_DuplicateApplyWithLeader() throws Exception {

        // Given
        User user1 = testUserFactory.generateUser(1);
        Project project = testProjectFactory.generateProject(1, user1, State.RECRUTING);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(108))
                .andDo(document("108"))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원_프로젝트 참여 인원이 중복 지원")
    @WithMockUser(username = "user1")
    void applyProjectFailBecause_DuplicateApplyWithProjectMember() throws Exception {

        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user1 = testUserFactory.generateUser(1);
        testProjectFactory.generateProjectMember(user1, project, true);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(108))
                .andDo(print())
        ;

    }

    @Test
    @DisplayName("프로젝트 지원_동일 사용자가 중복 지원")
    @WithMockUser(username = "user1")
    void applyProjectFailBecause_DuplicateApplyWithUser() throws Exception {

        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user1 = testUserFactory.generateUser(1);
        ProjectApply projectApply = testProjectFactory.generateApply(project, user1);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(108))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("프로젝트 지원하기_로그인하지 않은 사용자")
    void acceptMemberFailBecause_notLoginUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;

    }

    @Test
    @DisplayName("프로젝트 지원하기_탈퇴했거나 제재당한 회원")
    @WithMockUser(username = "user1")
    void applyProjectFailBecause_cannotAccessUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user1=testUserFactory.generateUser(1, UserActivate.BLOCKED);
        ProjectApplyRequest projectApplyRequest = testProjectFactory.generateProjectApplyRequest();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(202))
                .andDo(print())
                .andDo(document("202"))

        ;

    }
}
