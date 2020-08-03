package com.eskiiimo.web.projects.apply;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 지원자 리스트 확인하기")
public class GetApplicantsTest extends BaseControllerTest {


    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 확인하기_팀장일 때")
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

}