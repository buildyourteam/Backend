package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.repository.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

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

@DisplayName("프로젝트 리스트")
class ProjectListControllerTests extends BaseControllerTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @Transactional
    @DisplayName("검색기능사용 없이 전체리스트 조회")
    void queryProjectsTotal() throws Exception {
        // Given
        IntStream.range(0,30).forEach(i -> {
            this.generateEvent(i);
        });

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "1")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
        ;

    }


    @Test
    @Transactional
    @DisplayName("직군별 and 분야별 프로젝트 리스트 조회하기")
    void queryProjectsOccupationAndField() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("occupation","developer")
                .param("field", ProjectField.WEB.toString())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
                .andDo(document("get-projects",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("project-list").description("link to project list"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestParameters(
                                parameterWithName("page").description("찾은 페이지"),
                                parameterWithName("size").description("한 페이지당 프로젝트 갯수"),
                                parameterWithName("sort").description("정렬하여 paging하는 경우 기준"),
                                parameterWithName("occupation").description("프로젝트 직군"),
                                parameterWithName("field").description("프로젝트 분야")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(

                                fieldWithPath("_embedded.projectList[].projectId").description("프로젝트 아이디 (=이미지 파일 이름)"),
                                fieldWithPath("_embedded.projectList[].projectName").description("프로젝트 이름"),
                                fieldWithPath("_embedded.projectList[].teamName").description("팀명"),
                                fieldWithPath("_embedded.projectList[].endDate").description("마감일"),
                                fieldWithPath("_embedded.projectList[].description").description("프로젝트에 대한 설명"),
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
                                fieldWithPath("_embedded.projectList[].status").description("프로젝트 상태(모집중, 진행중, 마감)"),
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
                                fieldWithPath("_embedded.projectList[]._links.self.href").description("프로젝트 상세페이지로 가는 링크"),
                                fieldWithPath("_embedded.projectList[]._links.projectImage.href").description("프로젝트 이미지"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.project-list.href").description("프로젝트 리스트로 가는 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서"),
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
    @DisplayName("only 직군별 프로젝트 리스트 조회하기")
    void queryProjectsOccupation() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/projects")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
                .param("occupation","developer")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
        ;

    }

    @Test
    @Transactional
    @DisplayName("only 분야별로만 프로젝트 리스트 조회하기")
    void queryProjectsField() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

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
                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andExpect(jsonPath("_links.project-list").exists())
        ;

    }

    @Test
    @Transactional
    @DisplayName("마감임박한 프로젝트 리스트 조회")
    void DeadlineProjectList() throws Exception {
        // Given
        this.generateProjectDeadline(0);

        // When & Then
        this.mockMvc.perform(get("/projects/deadline")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andDo(document("get-deadline-project",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("deadline-project-list").description("link to deadline project list"),
                                linkWithRel("profile").description("link to profile")
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
                                fieldWithPath("_embedded.projectList[].description").description("프로젝트에 대한 설명"),
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
                                fieldWithPath("_embedded.projectList[].status").description("프로젝트 상태(모집중, 진행중, 마감)"),
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
                                fieldWithPath("_embedded.projectList[]._links.self.href").description("프로젝트 상세페이지로 가는 링크"),
                                fieldWithPath("_embedded.projectList[]._links.projectImage.href").description("프로젝트 이미지"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.deadline-project-list.href").description("마감 임박한 프로젝트 리스트로 가는 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서"),
                                fieldWithPath("page.size").description("한 페이지 당 프로젝트 갯수"),
                                fieldWithPath("page.totalElements").description("총 프로젝트 갯수"),
                                fieldWithPath("page.totalPages").description("총 페이지 수"),
                                fieldWithPath("page.number").description("페이지 수")

                        )
                ))
        ;

    }



    private void generateEvent(int index) {

        ProjectMemberSet need_zero = new ProjectMemberSet(0,2,3,4);
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet currentMember = new ProjectMemberSet(2,1,1,2);

        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
                .needMember(need_yes)
                .projectField(ProjectField.APP)
                .build();

        Project project1 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
                .projectField(ProjectField.WEB)
                .build();

        Project project2 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
                .needMember(need_yes)
                .projectField(ProjectField.WEB)
                .build();

        this.projectRepository.save(project);
        this.projectRepository.save(project1);
        this.projectRepository.save(project2);

    }
    private void generateProjectDeadline(int index) {

        ProjectMemberSet need_zero = new ProjectMemberSet(0,2,3,4);
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet currentMember = new ProjectMemberSet(2,1,1,2);

        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,2,28,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
                .needMember(need_yes)
                .build();

        Project project1 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,2,14,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
                .build();

        Project project2 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,03,30,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
                .build();

        this.projectRepository.save(project);
        this.projectRepository.save(project1);
        this.projectRepository.save(project2);

    }

}
