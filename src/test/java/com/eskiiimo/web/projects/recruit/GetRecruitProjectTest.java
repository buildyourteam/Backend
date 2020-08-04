package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

public class GetRecruitProjectTest extends BaseControllerTest {
    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 영입제안 확인하기(열람시 읽음상태로 전환)")
    void getRecruitProject() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("READ"))
                .andDo(print())
                .andDo(document("getRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("state").description("상태"),
                                fieldWithPath("projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("projectName").description("영입 제안 프로젝트 이름"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.acceptRecruit.href").description("영입 승인하기"),
                                fieldWithPath("_links.rejectRecruit.href").description("영입 거절하기"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")

                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("acceptRecruit").description("영입 승인하기"),
                                linkWithRel("rejectRecruit").description("영입 거절하기"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;

    }

    @Test
    @Transactional
    @WithMockUser(username = "user4")
    @DisplayName("나한테 온 영입제안 확인하기_권한없는 사용자")
    void getRecruitProject_AuthX() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 영입제안 확인하기_권한없는 사용자2")
    void getRecruitProject_AuthX2() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", "authX", projects.get(0).getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 영입제안 확인하기_존재하지 않는 영입제안")
    void getRecruitProject_notExist() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        String userId = myProject.getLeaderId();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", userId, myProject.getProjectId()))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }
}