package com.eskiiimo.web.user.profile;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("참여중인 프로젝트")
public class GetRunningProjectsTest extends BaseControllerTest {

    @Test
    @DisplayName("사용자가 참여중인 프로젝트 리스트 가져오기")
    public void getRunningProjectListSuccess() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);
        testProjectFactory.generateProject(1, user1, State.RUNNING);
        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/running")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-running-project",
                        requestParameters(
                                parameterWithName("page").description("찾은 페이지"),
                                parameterWithName("size").description("한 페이지당 프로젝트 갯수"),
                                parameterWithName("sort").description("정렬하여 paging하는 경우 기준")
                        )
                ))
        ;

    }

    @Test
    @DisplayName("사용자가 참여중인 프로젝트 리스트 가져오기_참여중인 프로젝트가 없을 때")
    public void getRunningProjectList_NotExistSuccess() throws Exception {
        // Given
        testUserFactory.generateUser(1);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/running")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}
