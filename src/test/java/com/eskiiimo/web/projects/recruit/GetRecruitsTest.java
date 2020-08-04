package com.eskiiimo.web.projects.recruit;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.request.RecruitProjectRequest;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("내가 보낸 영입제안 리스트 확인하기")
public class GetRecruitsTest extends BaseControllerTest {
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
    @Transactional
    @DisplayName("내가 보낸 영입제안 리스트 확인하기_권한 없는 사용자")
    @WithMockUser(username = "user1")
    public void getRecruits_authX() throws Exception {
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
    @Transactional
    @DisplayName("내가 보낸 영입제안 리스트 확인하기_영입제안이 없을 때")
    @WithMockUser(username = "user0")
    public void getRecruits_notExist() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);


        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/recruits", project.getProjectId()))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }

}
