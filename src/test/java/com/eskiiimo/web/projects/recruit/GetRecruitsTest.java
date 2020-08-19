package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("내가 보낸 영입제안 리스트 확인하기")
public class GetRecruitsTest extends BaseControllerTest {
    @Test
    @DisplayName("내가 보낸 영입제안 리스트 확인하기")
    @WithMockUser(username = "user0")
    public void getRecruitsSuccess() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        testProjectFactory.generateRecruit(testUserFactory.generateUser(1), project);
        testProjectFactory.generateRecruit(testUserFactory.generateUser(2), project);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/recruits", project.getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getSendRecruits",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )
                ));
        ;
    }

    @Test
    @DisplayName("내가 보낸 영입제안 리스트 확인하기_권한 없는 사용자")
    @WithMockUser(username = "user1")
    public void getRecruitsFailBecause_noPermittedUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        testProjectFactory.generateRecruit(testUserFactory.generateUser(1), project);
        testProjectFactory.generateRecruit(testUserFactory.generateUser(2), project);


        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/recruits", project.getProjectId()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(107))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("내가 보낸 영입제안 리스트 확인하기_로그인하지 않은 사용자")
    public void getRecruitsFailBecause_notLoginUser() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        testProjectFactory.generateRecruit(testUserFactory.generateUser(1), project);
        testProjectFactory.generateRecruit(testUserFactory.generateUser(2), project);


        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/recruits", project.getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("내가 보낸 영입제안 리스트 확인하기_영입제안이 없을 때")
    @WithMockUser(username = "user0")
    public void getRecruitsFailBecause_notExistRecruit() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);


        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/recruits", project.getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

}
