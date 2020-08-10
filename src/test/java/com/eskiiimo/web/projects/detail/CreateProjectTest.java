package com.eskiiimo.web.projects.detail;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 생성하기")
public class CreateProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 생성 성공")
    public void createProjectSuccess() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest project = testProjectFactory.generateProjectDetailRequest(myProject);

        // When & Then
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("create-project"))
        ;
        mockMvc.perform(get("/projects/", myProject.getProjectId()))
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 생성_필수 필드 누락")
    public void createProjectFailBecause_FieldMissing() throws Exception {
        // Given
        testUserFactory.generateUser(0);
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("question1").build());
        questions.add(ProjectApplyQuestion.builder().question("question2").build());
        ProjectDetailRequest project = ProjectDetailRequest.builder()
                .projectName("project1")
//                .teamName("Team1")
                .endDate(LocalDateTime.now().plusDays(10))
                .needMember(new ProjectMemberSet(3, 4, 4, 5))
                .projectField(ProjectField.WEB)
                .applyCanFile(Boolean.TRUE)
                .questions(questions)
                .build();

        // When & Then
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value(400))
                .andDo(document("400"))
        ;
    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 생성_잘못된 마감일")
    public void createProjectFailBecause_WrongEndDate() throws Exception {
        // Given
        testUserFactory.generateUser(0);
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("question1").build());
        questions.add(ProjectApplyQuestion.builder().question("question2").build());
        ProjectDetailRequest project = ProjectDetailRequest.builder()
                .projectName("project1")
                .teamName("Team1")
                .endDate(LocalDateTime.now().minusDays(10))
                .needMember(new ProjectMemberSet(3, 4, 4, 5))
                .projectField(ProjectField.WEB)
                .applyCanFile(Boolean.TRUE)
                .questions(questions)
                .build();

        // When & Then
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value(106))
                .andDo(document("106"))
        ;
    }

    @Test
    @DisplayName("프로젝트 생성_로그인하지 않은 사용자")
    public void createProjectFailBecause_notLoginUser() throws Exception {
        // Given
        testUserFactory.generateUser(0);
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("question1").build());
        questions.add(ProjectApplyQuestion.builder().question("question2").build());
        ProjectDetailRequest project = ProjectDetailRequest.builder()
                .projectName("project1")
                .teamName("Team1")
                .endDate(LocalDateTime.now().plusDays(10))
                .needMember(new ProjectMemberSet(3, 4, 4, 5))
                .projectField(ProjectField.WEB)
                .applyCanFile(Boolean.TRUE)
                .questions(questions)
                .build();

        // When & Then
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}
