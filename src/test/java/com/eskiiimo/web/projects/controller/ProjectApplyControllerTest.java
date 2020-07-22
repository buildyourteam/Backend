package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.ProjectApplyDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("프로젝트 지원하기")
class ProjectApplyControllerTest extends BaseControllerTest {

    @Test
    @Transactional
    @DisplayName("프로젝트 지원하고 내 지원서 확인하기")
    @WithMockUser(username = "user1")
    void applyProject() throws Exception{
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User me = testUserFactory.generateUser(1);
        ProjectApplyDto projectApplyDto = testProjectFactory.generateProjectApplyDto();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply",project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("applyProject",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("유저이름(NULL)"),
                                fieldWithPath("state").description("상태(NULL)"),
                                fieldWithPath("questions").description("지원서 질문(NULL)"),
                                fieldWithPath("answers").description("지원서 응답"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("introduction").description("자기소개")
                        )
                        ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),me.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userName").value("user1"))
                .andExpect(jsonPath("role").value("DEVELOPER"))
                .andExpect(jsonPath("introduction").value("안녕하세요? 저는 그냥 개발자입니다."))
                .andExpect(jsonPath("answers[0]").value("1번 응답"))
                .andExpect(jsonPath("answers[1]").value("2번 응답"))
                .andExpect(jsonPath("answers[2]").value("3번 응답"))
        ;

    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 수정하기")
    @WithMockUser(username = "user1")
    void updateApply() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);
        ProjectApplyDto projectApplyDto = testProjectFactory.generateProjectApplyDto();
        projectApplyDto.setIntroduction("지원서 수정 완료!");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply",project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("updateApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("state").description("상태(NULL)"),
                                fieldWithPath("questions").description("지원서 질문"),
                                fieldWithPath("answers").description("지원서 응답"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("introduction").description("자기소개")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("introduction").value("지원서 수정 완료!"))
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 확인하기")
    @WithMockUser(username = "user0")
    void getApplicants() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(2);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getApplicants",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.projectApplicantDtoList[].userId").description("유저Id"),
                                fieldWithPath("_embedded.projectApplicantDtoList[].userName").description("유저이름"),
                                fieldWithPath("_embedded.projectApplicantDtoList[].state").description("상태"),
                                fieldWithPath("_embedded.projectApplicantDtoList[].role").description("지원할 역할"),
                                fieldWithPath("_embedded.projectApplicantDtoList[]._links.self.href").description("해당 지원자의 지원서 링크"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 확인하기_지원자가 없을때")
    @WithMockUser(username = "user0")
    void getApplicantsNoApply() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 확인하기_권한없는 사용자")
    @WithMockUser(username = "authX")
    void getApplicantsWrongUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);

        // When & Then
        this.mockMvc.perform(get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 하나 확인하기_팀장일때")
    @WithMockUser(username = "user0")
    void getApply() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"user1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("state").description("상태"),
                                fieldWithPath("questions").description("질문"),
                                fieldWithPath("answers").description("응답"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.acceptApply.href").description("지원서 승인하기"),
                                fieldWithPath("_links.rejectApply.href").description("지원서 거절하기"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("acceptApply").description("지원서 승인하기"),
                                linkWithRel("rejectApply").description("지원서 거절하기"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;

    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 수락하기")
    @WithMockUser(username = "user0")
    void acceptMember() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(),"user1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("acceptApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("ACCEPT"))
        ;
        this.mockMvc.perform(get("/projects/{projectId}", project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("memberList[1].userName").value("UserName4"))
                .andExpect(jsonPath("memberList[1].role").value("DEVELOPER"))
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 거절하기")
    @WithMockUser(username = "user0")
    void rejectMember() throws Exception {
        // Given
        Project project = testProjectFactory.generateProjectApplies(1);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{projectId}/apply/{userId}", project.getProjectId(),"user1"))
                .andExpect(status().isOk())
                .andDo(document("rejectApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("REJECT"))
        ;
    }
}