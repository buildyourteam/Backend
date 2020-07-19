package com.eskiiimo.web.projects.controller;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 리스트")
class ProjectListControllerTests extends BaseControllerTest {

    @Test
    @Transactional
    @DisplayName("검색기능사용 없이 전체리스트 조회")
    void queryProjectsTotal() throws Exception {
        // Given
        generateProjects();

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
        ;

    }


    @Test
    @Transactional
    @DisplayName("직군별 and 분야별 프로젝트 리스트 조회하기")
    void queryProjectsOccupationAndField() throws Exception {
        // Given
        generateProjects();

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("occupation", "developer")
                .param("field", ProjectField.WEB.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("get-projects",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestParameters(
                                parameterWithName("page").description("찾은 페이지"),
                                parameterWithName("size").description("한 페이지당 프로젝트 갯수"),
                                parameterWithName("sort").description("정렬하여 paging하는 경우 기준"),
                                parameterWithName("occupation").description("프로젝트 직군"),
                                parameterWithName("field").description("프로젝트 분야")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(

                                fieldWithPath("_embedded.projectList[].projectId").description("프로젝트 아이디 (=이미지 파일 이름)"),
                                fieldWithPath("_embedded.projectList[].projectName").description("프로젝트 이름"),
                                fieldWithPath("_embedded.projectList[].teamName").description("팀명"),
                                fieldWithPath("_embedded.projectList[].endDate").description("마감일"),
                                fieldWithPath("_embedded.projectList[].introduction").description("프로젝트에 대한 설명"),
                                fieldWithPath("_embedded.projectList[].state").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("_embedded.projectList[].projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("_embedded.projectList[].leaderId").description("팀장 아이디"),
                                fieldWithPath("_embedded.projectList[].currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("_embedded.projectList[].needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.etc").description("그 외 필요한 인원 수"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("page.size").description("한 페이지 당 프로젝트 갯수"),
                                fieldWithPath("page.totalElements").description("총 프로젝트 갯수"),
                                fieldWithPath("page.totalPages").description("총 페이지 수"),
                                fieldWithPath("page.number").description("페이지 수")

                        )
                ))
        ;

    }

    @Test
    @Transactional
    @DisplayName("only 직군별 프로젝트 리스트 조회하기")
    void queryProjectsOccupation() throws Exception {
        // Given
        generateProjects();

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("occupation", "developer")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
        ;

    }

    @Test
    @Transactional
    @DisplayName("only 분야별로만 프로젝트 리스트 조회하기")
    void queryProjectsField() throws Exception {
        // Given
        generateProjects();

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("field", ProjectField.WEB.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
        ;

    }

    @Transactional
    void generateProjects() {
        this.testProjectFactory.generateProject(0, ProjectField.AI, false);
        this.testProjectFactory.generateProject(1, ProjectField.APP, true);
        this.testProjectFactory.generateProject(2, ProjectField.WEB, true);
        this.testProjectFactory.generateProject(3, ProjectField.WEB, true);

    }


}
