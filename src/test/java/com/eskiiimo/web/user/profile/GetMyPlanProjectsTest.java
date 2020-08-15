package com.eskiiimo.web.user.profile;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.State;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("기획한 프로젝트")
public class GetMyPlanProjectsTest extends BaseControllerTest {

    @Test
    @DisplayName("사용자가 기획한 프로젝트 리스트 가져오기")
    public void getPlannedProjectListSuccess() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        testProjectFactory.generateProject(1, user1, State.RUNNING);
        testProjectFactory.generateProject(2, user1, State.RECRUTING);
        testProjectFactory.generateProject(3, user1, State.RUNNING);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/plan")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-planned-project",
                        requestParameters(
                                parameterWithName("page").description("찾은 페이지"),
                                parameterWithName("size").description("한 페이지당 프로젝트 갯수"),
                                parameterWithName("sort").description("정렬하여 paging하는 경우 기준")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("사용자가 기획한 프로젝트 리스트 가져오기_기획한 프로젝트가 없을 때")
    public void getPlannedProjectList_NotExistSuccess() throws Exception {
        // Given
        testUserFactory.generateUser(1);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/plan")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}
