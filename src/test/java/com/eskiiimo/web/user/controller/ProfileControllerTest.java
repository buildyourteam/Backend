package com.eskiiimo.web.user.controller;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.State;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로필")
class ProfileControllerTest extends BaseControllerTest {

    @Test
    @Transactional
    @DisplayName("본인의 프로필 조회")
    @WithMockUser(username = "user1")
    void getMyProfile() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}", "user1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("query-my-profile",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("updateProfile").description("프로필 업데이트"),
                                linkWithRel("recruits").description("나에게 온 영입제안들"),
                                linkWithRel("profile").description("Api 명세서")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("role").description("역할군"),
                                fieldWithPath("stacks").description("기술스택"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("area").description("활동지역"),
                                fieldWithPath("grade").description("레벨"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.updateProfile.href").description("프로필 업데이트"),
                                fieldWithPath("_links.recruits.href").description("나에게 온 영입제안들"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
        ;

    }


    @Test
    @Transactional
    @DisplayName("프로필 조회")
    void getProfile() throws Exception {
        testUserFactory.generateUser(1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}", "user1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("query-profile",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("role").description("역할군"),
                                fieldWithPath("stacks").description("기술스택"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("area").description("활동지역"),
                                fieldWithPath("grade").description("레벨"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
        ;

    }

    @Test
    @Transactional
    @DisplayName("프로필 수정")
    @WithMockUser(username = "user1")
    void updateProfile() throws Exception {
        testUserFactory.generateUser(1);
        ProfileDto profileDto = testProjectFactory.generateProfileDto();

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}", "user1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(profileDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("update-profile",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        ),
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("role").description("역할군"),
                                fieldWithPath("stacks").description("기술스택"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("area").description("활동지역"),
                                fieldWithPath("grade").description("레벨"),
                                fieldWithPath("introduction").description("자기소개")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("사용자 이름"),
                                fieldWithPath("role").description("역할군"),
                                fieldWithPath("stacks").description("기술스택"),
                                fieldWithPath("contact").description("연락처"),
                                fieldWithPath("area").description("활동지역"),
                                fieldWithPath("grade").description("레벨"),
                                fieldWithPath("introduction").description("자기소개"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
        ;
    }

    @Test
    @Transactional
    @DisplayName("사용자가 참여중인 프로젝트 리스트 가져오기")
    public void getRunningProjectList() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project1 = testProjectFactory.generateProject(1, user1, State.RUNNING);
        Project project2 = testProjectFactory.generateProject(2, user1, State.RECRUTING);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);

        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RECRUTING);
        Project project6 = testProjectFactory.generateProject(6, user2, State.RUNNING);

        testProjectFactory.generateProjectMember(user1, project4, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project1, Boolean.TRUE);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/running")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-running-project",
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
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
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
    @Transactional
    @DisplayName("사용자가 참여했던 프로젝트 리스트 가져오기")
    public void getEndedProjectList() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project1 = testProjectFactory.generateProject(1, user1, State.ENDED);
        Project project2 = testProjectFactory.generateProject(2, user1, State.ENDED);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);

        Project project4 = testProjectFactory.generateProject(4, user2, State.ENDED);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RECRUTING);
        Project project6 = testProjectFactory.generateProject(6, user2, State.RUNNING);

        testProjectFactory.generateProjectMember(user1, project4, Boolean.TRUE);
        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/ended")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-ended-project",
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
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
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
    @Transactional
    @DisplayName("사용자가 기획한 프로젝트 리스트 가져오기")
    public void getPlannedProjectList() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project1 = testProjectFactory.generateProject(1, user1, State.RUNNING);
        Project project2 = testProjectFactory.generateProject(2, user1, State.RECRUTING);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);

        testProjectFactory.generateProject(4, user2, State.RUNNING);
        testProjectFactory.generateProject(5, user2, State.RECRUTING);
        testProjectFactory.generateProject(6, user2, State.RUNNING);

        testProjectFactory.generateProjectMember(user1, project1, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project2, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project3, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(get("/profile/user1/plan")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-planned-project",
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
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
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
    @Transactional
    @DisplayName("사용자가 참여중인 숨겨진 프로젝트 리스트 가져오기")
    public void getRunningHiddenProjectList() throws Exception {
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
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
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
    @Transactional
    @DisplayName("사용자가 참여했던 숨겨진 프로젝트 리스트 가져오기")
    public void getEndedHiddenProjectList() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project1 = testProjectFactory.generateProject(1, user1, State.ENDED);
        Project project2 = testProjectFactory.generateProject(2, user1, State.ENDED);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);

        Project project4 = testProjectFactory.generateProject(4, user2, State.ENDED);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RECRUTING);
        Project project6 = testProjectFactory.generateProject(6, user2, State.RUNNING);

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
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
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
    @Transactional
    @WithMockUser(username = "user1")
    @DisplayName("사용자가 기획한 숨겨진 프로젝트 리스트 가져오기")
    public void getPlannedHiddenProjectList() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project1 = testProjectFactory.generateProject(1, user1, State.RUNNING);
        Project project2 = testProjectFactory.generateProject(2, user1, State.RECRUTING);
        Project project3 = testProjectFactory.generateProject(3, user1, State.RUNNING);

        testProjectFactory.generateProject(4, user2, State.RUNNING);
        testProjectFactory.generateProject(5, user2, State.RECRUTING);
        testProjectFactory.generateProject(6, user2, State.RUNNING);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/projects/{projectId}", user1.getUserId(), project2.getProjectId()))
                .andDo(print())
                .andExpect(status().isOk())
        ;
        // When & Then
        this.mockMvc.perform(get("/profile/user1/plan/hidden")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get-planned-hidden-project",
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
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
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
    @Transactional
    @WithMockUser(username = "user1")
    @DisplayName("프로젝트 숨기기")
    public void hideProject() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RUNNING);

        testProjectFactory.generateProjectMember(user1, project4, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project5, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("hideProject",
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디"),
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )))

        ;

    }

    @Test
    @Transactional
    @WithMockUser(username = "user1")
    @DisplayName("숨긴 프로젝트 취소하기")
    public void reShowProject() throws Exception {
        // Given
        User user1 = testUserFactory.generateUser(1);
        User user2 = testUserFactory.generateUser(2);

        Project project4 = testProjectFactory.generateProject(4, user2, State.RUNNING);
        Project project5 = testProjectFactory.generateProject(5, user2, State.RUNNING);

        testProjectFactory.generateProjectMember(user1, project4, Boolean.FALSE);
        testProjectFactory.generateProjectMember(user1, project5, Boolean.FALSE);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/projects/{projectId}", user1.getUserId(), project4.getProjectId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("reshowProject",
                        pathParameters(
                                parameterWithName("userId").description("사용자 아이디"),
                                parameterWithName("projectId").description("프로젝트 아이디")
                        )));
    }
}