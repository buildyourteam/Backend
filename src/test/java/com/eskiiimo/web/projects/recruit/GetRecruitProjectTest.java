package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

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

@DisplayName("나한테 온 영입제안 확인하기")
public class GetRecruitProjectTest extends BaseControllerTest {
    @Test
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 영입제안 확인하기(열람시 읽음상태로 전환)")
    void getRecruitProjectSuccess() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        Project project = testProjectFactory.generateMyProject(3);
        testProjectFactory.generateRecruit(me, project);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", me.getUserId(), project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("READ"))
                .andDo(print())
                .andDo(document("getRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
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
    @WithMockUser(username = "user4")
    @DisplayName("나한테 온 영입제안 확인하기_권한없는 사용자")
    void getRecruitProjectFailBecause_noPermittedUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        Project project = testProjectFactory.generateMyProject(3);
        testProjectFactory.generateRecruit(me, project);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", me.getUserId(), project.getProjectId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(104))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("나한테 온 영입제안 확인하기_로그인하지 않은 사용자")
    void getRecruitProjectFailBecause_notLoginUser() throws Exception {
        // Given
        User me = testUserFactory.generateUser(0);
        Project project = testProjectFactory.generateMyProject(3);
        testProjectFactory.generateRecruit(me, project);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", me.getUserId(), project.getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 영입제안 확인하기_존재하지 않는 영입제안")
    void getRecruitProjectFailBecause_notExistRecruit() throws Exception {
        // Given
        Project myProject = testProjectFactory.generateMyProject(0);
        String userId = myProject.getLeaderId();

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", userId, myProject.getProjectId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value(105))
                .andDo(print())
        ;
    }
}
