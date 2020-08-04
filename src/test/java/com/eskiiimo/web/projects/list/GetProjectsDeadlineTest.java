package com.eskiiimo.web.projects.list;

import com.eskiiimo.web.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("마감일 순으로 프로젝트 조회")
public class GetProjectsDeadlineTest extends BaseControllerTest {
    @Test
    @Transactional
    @DisplayName("마감일 순으로 프로젝트 조회")
    void queryDeadlineProjectsTotal() throws Exception {
        // Given
        testProjectFactory.generateProjects();

        // When & Then
        this.mockMvc.perform(get("/projects/deadline")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "")
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;

    }


}
