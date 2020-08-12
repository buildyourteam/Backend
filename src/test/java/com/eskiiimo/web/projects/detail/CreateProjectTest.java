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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
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
                .andDo(document("create-project",
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("introduction").description("프로젝트에 대한 설명"),
                                fieldWithPath("state").description("프로젝트 상태(모집중)"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        )
                ))
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
                .endDate(LocalDateTime.of(2022, 05, 20, 11, 11))
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
                .andExpect(jsonPath("error").value(403))
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
                .endDate(LocalDateTime.of(2022, 05, 20, 11, 11))
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
