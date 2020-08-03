package com.eskiiimo.web.projects.detail;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UpdateProjectTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 수정하기_팀장일 때")
    @Transactional
    public void updateProject_leader() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest projectDetailRequest = testProjectFactory.generateProjectUpdateRequest(myProject);
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
    @WithMockUser(username = "user1")
    @DisplayName("프로젝트 수정하기_팀장이 아닐 때")
    @Transactional
    public void updateProject_notLeader() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        ProjectDetailRequest projectDetailRequest = testProjectFactory.generateProjectUpdateRequest(myProject);
        projectDetailRequest.setProjectName("Hi updated project....");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{project_id}", myProject.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(projectDetailRequest)))
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}
