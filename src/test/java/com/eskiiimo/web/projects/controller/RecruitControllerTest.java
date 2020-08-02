package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.RecruitDto;
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
import java.util.List;
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

@DisplayName("프로젝트 영입하기")
class RecruitControllerTest extends BaseControllerTest {

    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("프로젝트 영입하기")
    void recruitProject() throws Exception {
        // Given
        Project project = testProjectFactory.generateMyProject(0);
        User user = testUserFactory.generateUser(1);
        RecruitDto recruitDto = testProjectFactory.generateRecruitDto(project.getProjectId(), user);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit",user.getUserId())
                .content(objectMapper.writeValueAsString(recruitDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("projectRecruit",
                        pathParameters(
                                parameterWithName("userId").description("유저 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("state").description("상태"),
                                fieldWithPath("projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("projectName").description("영입 제안 프로젝트 이름"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("introduction").description("자기소개")
                        )
                ));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트")
    void getRecruitList() throws Exception{
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", userId))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getRecruits",
                        pathParameters(
                                parameterWithName("userId").description("유저 아이디")
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
    }



    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("나한테 온 영입제안 확인하기(열람시 읽음상태로 전환)")
    void getRecruitProject() throws Exception{
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
    @WithMockUser(username = "user0")
    @DisplayName("영입제안 승락하기")
    void acceptRecruitProject() throws Exception{
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("acceptRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ));

        this.mockMvc.perform(get("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("ACCEPT"))
        ;
    }

    @Test
    @Transactional
    @WithMockUser(username = "user0")
    @DisplayName("영입제안 거절하기")
    void rejectRecruitProject() throws Exception{
        // Given
        User me = testUserFactory.generateUser(0);
        String userId = me.getUserId();
        List<Project> projects = testProjectFactory.generateProjectRecruits(2, me);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("rejectRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;

        this.mockMvc.perform(get("/profile/{userId}/recruit/{projectId}", userId, projects.get(0).getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("state").value("REJECT"));
    }
}