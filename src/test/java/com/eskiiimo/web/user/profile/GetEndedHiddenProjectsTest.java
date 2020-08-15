package com.eskiiimo.web.user.profile;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;

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

@DisplayName("참여했던 숨겨진 프로젝트")
public class GetEndedHiddenProjectsTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("사용자가 참여했던 숨겨진 프로젝트 리스트 가져오기")
    public void getEndedHiddenProjectListSuccess() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        Project project1 = testProjectFactory.generateProject(1, user2, State.ENDED);
        Project project4 = testProjectFactory.generateProject(4, user2, State.ENDED);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/ended/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-ended-hidden-project",
                        requestParameters(
                                parameterWithName("page").description("찾은 페이지"),
                                parameterWithName("size").description("한 페이지당 프로젝트 갯수"),
                                parameterWithName("sort").description("정렬하여 paging하는 경우 기준")
                        )
                ))

        ;

    }

    @Test
    @WithMockUser(username = "user0")
    @DisplayName("사용자가 참여했던 숨겨진 프로젝트 리스트 가져오기_권한없는 사용자")
    public void getEndedHiddenProjectListFailBecause_noPermittedUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        Project project1 = testProjectFactory.generateProject(1, user2, State.ENDED);
        Project project4 = testProjectFactory.generateProject(4, user2, State.ENDED);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/ended/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(201))
                .andDo(document("201"))
        ;
    }

    @Test
    @DisplayName("사용자가 참여했던 숨겨진 프로젝트 리스트 가져오기_로그인하지 않은 사용자")
    public void getEndedHiddenProjectListFailBecause_notLoginUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        Project project1 = testProjectFactory.generateProject(1, user2, State.ENDED);
        Project project4 = testProjectFactory.generateProject(4, user2, State.ENDED);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/ended/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("사용자가 참여했던 숨겨진 프로젝트 리스트 가져오기_프로젝트가 없을 때")
    public void getEndedHiddenProjectList_NotExistSuccess() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/profile/user1/ended/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}
