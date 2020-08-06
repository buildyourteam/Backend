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

@DisplayName("참여중인 숨겨진 프로젝트")
public class GetRunningHiddenProjectsTest extends BaseControllerTest {

    @Test
    @WithMockUser(username = "user1")
    @DisplayName("사용자가 참여중인 숨겨진 프로젝트 리스트 가져오기")
    public void getRunningHiddenProjectListSuccess() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project1 = testProjectFactory.generateProject(1, user1, State.RUNNING);
        Project project2 = testProjectFactory.generateProject(2, user1, State.RECRUTING);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);

        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RECRUTING);
        Project project6 = testProjectFactory.generateProject(6, user2, State.RUNNING);

        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project2, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project3, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project5, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project6, Boolean.TRUE);


        // When & Then
        this.mockMvc.perform(get("/profile/user1/running/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-running-hidden-project",
                        links(
                                linkWithRel("self").description("link to self")
                        ),
                        requestParameters(
                                parameterWithName("page").description("page"),
                                parameterWithName("size").description("number of projects per page"),
                                parameterWithName("sort").description("sort")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(

                                fieldWithPath("_embedded.projectList[].projectId").description("프로젝트 아이디 (=이미지 파일 이름)"),
                                fieldWithPath("_embedded.projectList[].projectName").description("프로젝트 이름"),
                                fieldWithPath("_embedded.projectList[].teamName").description("팀명"),
                                fieldWithPath("_embedded.projectList[].endDate").description("마감일"),
                                fieldWithPath("_embedded.projectList[].introduction").description("프로젝트에 대한 설명"),
                                fieldWithPath("_embedded.projectList[].state").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("_embedded.projectList[].projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("_embedded.projectList[].leaderId").description("팀장 아이디"),
                                fieldWithPath("_embedded.projectList[].currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("_embedded.projectList[].needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.etc").description("그 외 필요한 인원 수"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("page.size").description("한 페이지 당 프로젝트 갯수"),
                                fieldWithPath("page.totalElements").description("총 프로젝트 갯수"),
                                fieldWithPath("page.totalPages").description("총 페이지 수"),
                                fieldWithPath("page.number").description("페이지 수")

                        )
                ))
        ;

    }

    @Test
    @WithMockUser(username = "user2")
    @DisplayName("사용자가 참여중인 숨겨진 프로젝트 리스트 가져오기_권한 없는 사용자")
    public void getRunningHiddenProjectListFailBecause_noAuthUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        Project project1 = testProjectFactory.generateProject(1, user1, State.RUNNING);
        Project project2 = testProjectFactory.generateProject(2, user1, State.RECRUTING);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);
        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project2, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project3, Boolean.FALSE);


        // When & Then
        this.mockMvc.perform(get("/profile/user1/running/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("error").value(201))
        ;
    }

    @Test
    @DisplayName("사용자가 참여중인 숨겨진 프로젝트 리스트 가져오기_로그인하지 않은 사용자")
    public void getRunningHiddenProjectListFailBecause_notLoginUser() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        Project project1 = testProjectFactory.generateProject(1, user1, State.RUNNING);
        Project project2 = testProjectFactory.generateProject(2, user1, State.RECRUTING);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);
        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project2, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project3, Boolean.FALSE);


        // When & Then
        this.mockMvc.perform(get("/profile/user1/running/hidden")
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
    @DisplayName("사용자가 참여중인 숨겨진 프로젝트 리스트 가져오기_숨겨진 프로젝트가 없을 때")
    public void getRunningHiddenProjectList_NotExistSuccess() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/profile/user1/running/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

}