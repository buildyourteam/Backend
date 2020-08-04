package com.eskiiimo.web.projects.detail;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 상세 페이지 확인하기")
public class GetProjectDetailTest extends BaseControllerTest {

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
    @DisplayName("프로젝트 상세 페이지 확인하기_없는 프로젝트일 때")
    @Transactional
    void getProjectDetail_notExist() throws Exception {
        // Given

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}", (long) 1))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }
}
