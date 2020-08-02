package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import com.eskiiimo.web.projects.request.RecruitProjectRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("프로젝트 상세 페이지")
class ProjectDetailControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("프로젝트 상세 페이지 확인하기")
    @Transactional
    void getProjectDetail() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user2 = testUserFactory.generateUser(1);
        testProjectFactory.generateProjectMember(user2, project, false);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}", project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("projectName").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
                .andDo(document("query-project",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("apply").description("apply to project"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("projectId").description("Project id")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("introduction").description("프로젝트에 대한 설명"),
                                fieldWithPath("state").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("currentMember.designer").description("현재 디자이너 수r"),
                                fieldWithPath("currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("currentMember.etc").description("현재 기타 인원수"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("memberList[].userName").description("프로젝트 팀원의 이름"),
                                fieldWithPath("memberList[].role").description("프로젝트 팀원의 역할"),
                                fieldWithPath("memberList[].stack").description("프로젝트 팀원의 기술스택"),
                                fieldWithPath("memberList[].grade").description("프로젝트 팀원의 레벨"),
                                fieldWithPath("memberList[]._links.self.href").description("프로젝트 팀원의 프로필"),
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("_links.self.href").description("Self 링크"),
                                fieldWithPath("_links.apply.href").description("프로젝트 지원 링크"),
                                fieldWithPath("_links.profile.href").description("API 명세서")


                        )
                ))
        ;
    }

    @Test
    @Transactional
    @DisplayName("내가 보낸 영입제안 리스트 확인하기")
    @WithMockUser(username = "user0")
    public void getRecruits() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        Long projectId = project.getProjectId();
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        RecruitProjectRequest recruitProjectRequest1 = testProjectFactory.generateRecruitRequest(projectId, user1);
        RecruitProjectRequest recruitProjectRequest2 = testProjectFactory.generateRecruitRequest(projectId, user2);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", "user1", projectId)
                .content(objectMapper.writeValueAsString(recruitProjectRequest1))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit", "user2", projectId)
                .content(objectMapper.writeValueAsString(recruitProjectRequest2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/recruits", projectId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getSendRecruits",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.recruitDtoList[].userName").description("유저이름"),
                                fieldWithPath("_embedded.recruitDtoList[].introduction").description("자기소개"),
                                fieldWithPath("_embedded.recruitDtoList[].role").description("지원할 역할"),
                                fieldWithPath("_embedded.recruitDtoList[].state").description("상태"),
                                fieldWithPath("_embedded.recruitDtoList[].projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("_embedded.recruitDtoList[].projectName").description("영입 제안 프로젝트 이름"),
                                fieldWithPath("_embedded.recruitDtoList[]._links.self.href").description("self 링크"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ));
        ;
    }

    @Test
    @WithMockUser(username = "user0")
    @Transactional
    @DisplayName("프로젝트 생성하기")
    public void createProject() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest project = testProjectFactory.generateProjectDetailRequest(myProject);

        // When & Then
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("create-project",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("createdProject").description("생성된 프로젝트 링크"),
                                linkWithRel("profile").description("API 명세서")
                        ),
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
    @Transactional
    @DisplayName("프로젝트 생성 실패")
    public void createProjectFailed() throws Exception {
        // Given
        testUserFactory.generateUser(0);
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("question1").build());
        questions.add(ProjectApplyQuestion.builder().question("question2").build());
        ProjectDetailRequest project = ProjectDetailRequest.builder()
                .projectName("project1")
//                .teamName("Team1")
                .endDate(LocalDateTime.of(2022, 05, 20, 11, 11))
//                .description("Hi this is project1.")
                .needMember(new ProjectMemberSet(3, 4, 4, 5))
                .projectField(ProjectField.WEB)
                .applyCanFile(Boolean.TRUE)
                .questions(questions)
                .build();

        // When & Then
        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 수정하기")
    @Transactional
    public void updateProject() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest projectDetailRequest = testProjectFactory.generateProjectUpdateDto(myProject);
        projectDetailRequest.setProjectName("Hi updated project....");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{project_id}", myProject.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(projectDetailRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-project",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("project_id").description("Project id")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("introduction").description("프로젝트에 대한 설명"),
                                fieldWithPath("state").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("introduction").description("프로젝트에 대한 설명"),
                                fieldWithPath("state").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("memberList").description("프로젝트에 참가하는 멤버 리스트"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("memberList[].userName").description("프로젝트 팀원의 이름"),
                                fieldWithPath("memberList[].role").description("프로젝트 팀원의 역할"),
                                fieldWithPath("memberList[].stack").description("프로젝트 팀원의 기술스택"),
                                fieldWithPath("memberList[].grade").description("프로젝트 팀원의 레벨"),
                                fieldWithPath("memberList[]._links.self.href").description("프로젝트 팀원의 프로필"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
        ;
    }

    @Test
    @WithMockUser(username = "user0")
    @Transactional
    @DisplayName("프로젝트 삭제하기")
    public void deleteProject() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{project_id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("delete-project",
                        pathParameters(
                                parameterWithName("project_id").description("Project id")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        )
                ));
    }

}