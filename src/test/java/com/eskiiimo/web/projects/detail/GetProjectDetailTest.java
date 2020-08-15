package com.eskiiimo.web.projects.detail;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 상세 페이지 확인하기")
public class GetProjectDetailTest extends BaseControllerTest {

    @Test
    @DisplayName("프로젝트 상세 페이지 확인하기")
    void getProjectDetailSuccess() throws Exception {
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
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("apply").description("프로젝트 지원하기"),
                                linkWithRel("profile").description("Api 명세서")
                        ),
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )
                ))
        ;
    }

    @Test
    @WithMockUser("user0")
    @DisplayName("프로젝트 상세 페이지 확인하기_내 프로젝트")
    void getMyProjectDetailSuccess() throws Exception {
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
                .andDo(document("query-my-project",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("updateProject").description("프로젝트 수정하기"),
                                linkWithRel("deleteProject").description("프로젝트 삭제하기"),
                                linkWithRel("getApplicants").description("지원자 정보 조회하기"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("프로젝트 상세 페이지 확인하기_없는 프로젝트일 때")
    void getProjectDetailFailBecause_notExistProject() throws Exception {
        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}", (long) 1))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(103))
                .andDo(print())
        ;
    }
}
