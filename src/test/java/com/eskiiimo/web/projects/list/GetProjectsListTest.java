package com.eskiiimo.web.projects.list;

import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

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

@DisplayName("프로젝트 리스트 조회")
public class GetProjectsListTest extends BaseControllerTest {
    @Test
    @DisplayName("검색기능사용 없이 전체리스트 조회")
    void queryProjectsTotalSuccess() throws Exception {
        // Given
        testProjectFactory.generateProjects();

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
    @DisplayName("검색기능사용 없이 전체리스트 조회_프로젝트 리스트가 없을 때")
    void queryProjectsTotal_notExistSuccess() throws Exception {
        // Given

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
    @DisplayName("직군별 and 분야별 프로젝트 리스트 조회하기")
    void queryProjectsOccupationAndFieldSuccess() throws Exception {
        // Given
        testProjectFactory.generateProjects();

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "0")
                .param("size", "1")
                .param("sort", "projectName,DESC")
                .param("occupation", "developer")
                .param("field", ProjectField.WEB.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("get-projects",
                        requestParameters(
                                parameterWithName("page").description("찾은 페이지"),
                                parameterWithName("size").description("한 페이지당 프로젝트 갯수"),
                                parameterWithName("sort").description("정렬 기준"),
                                parameterWithName("occupation").description("프로젝트 직군"),
                                parameterWithName("field").description("프로젝트 분야")
                        )
                ))
        ;

    }

    @Test
    @DisplayName("only 직군별 프로젝트 리스트 조회하기")
    void queryProjectsOccupationSuccess() throws Exception {
        // Given
        testProjectFactory.generateProjects();

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
    @DisplayName("only 분야별로만 프로젝트 리스트 조회하기")
    void queryProjectsFieldSuccess() throws Exception {
        // Given
        testProjectFactory.generateProjects();

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

}
