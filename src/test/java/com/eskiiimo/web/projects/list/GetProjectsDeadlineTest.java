package com.eskiiimo.web.projects.list;

import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("마감일 순으로 프로젝트 조회")
public class GetProjectsDeadlineTest extends BaseControllerTest {
    @Test
    @DisplayName("마감일 순으로 프로젝트 조회")
    void queryDeadlineProjectsTotalSuccess() throws Exception {
        // Given
        testProjectFactory.generateProjects();

        // When & Then
        this.mockMvc.perform(get("/projects/deadline")
                .param("page", "0")
                .param("size", "1")
                .param("sort", "endDate")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-deadline-projects",
                requestParameters(
                        parameterWithName("page").description("찾은 페이지"),
                        parameterWithName("size").description("한 페이지당 프로젝트 갯수"),
                        parameterWithName("sort").description("정렬 기준")
                )
        ))
        ;

    }


}
