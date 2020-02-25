package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.ProjectMemberSet;
import com.eskiiimo.api.projects.ProjectRepository;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme= "https",uriHost = "api.eskiiimo.com" ,uriPort = 443)
@Import(RestDocsConfiguration.class)
class ProjectDetailControllerTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;



    @Test
    void getProjectDetailNoMembers () throws Exception {
        // Given
            this.generateProject(2);


        // When & Then
        this.mockMvc.perform(get("/api/projects/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(document("query-events"))
        ;
    }
    @Test
    void getProjectDetail() throws Exception {
        // Given
        this.generateProject(1);
        this.joinProjectMember((long)1,1);
        this.joinProjectMember((long)1,2);
        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/api/projects/{projectId}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("projectName").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andDo(print())
                .andDo(document("query-project",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("apply").description("apply to project"),
                                linkWithRel("profile").description("link to profile")
                            ),
                        pathParameters(
                                parameterWithName("projectId").description("Project id")
                            ),
                        responseHeaders(
                               headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                            ),
                        responseFields(
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("description").description("프로젝트에 대한 설명"),
                                fieldWithPath("status").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("currentMember.designer").description("현재 디자이너 수r"),
                                fieldWithPath("currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("currentMember.etc").description("현재 기타 인원수"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("memberList[].userName").description("프로젝트 팀원의 이름"),
                                fieldWithPath("memberList[].role").description("프로젝트 팀원의 역할"),
                                fieldWithPath("memberList[].stack").description("프로젝트 팀원의 기술스택"),
                                fieldWithPath("memberList[].level").description("프로젝트 팀원의 레벨"),
                                fieldWithPath("memberList[]._links.self.href").description("프로젝트 팀원의 프로필"),
                                fieldWithPath("_links.self.href").description("Self 링크"),
                                fieldWithPath("_links.apply.href").description("프로젝트 지원 링크"),
                                fieldWithPath("_links.profile.href").description("API 명세서")


                        )
                 ))
        ;
    }


    private void generateProject(int index) {

        ProjectMemberSet need_zero = new ProjectMemberSet(0,2,3,4);
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet current = new ProjectMemberSet(2,1,1,2);

        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(current)
                .needMember(need_yes)
                .status(ProjectStatus.RECRUTING)
                .build();

        this.projectRepository.save(project);

    }


    private void joinProjectMember(Long index,int memberno){
        Optional<Project> optionalProject = this.projectRepository.findById(index);
        Project project = optionalProject.get();
        generateUser(memberno);
        Optional<User> optionalMember = this.userRepository.findById((long)memberno);
        User user =optionalMember.get();
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.DEVELOPER)
                .stack(TechnicalStack.SPRINGBOOT)
                .project(project)
                .selfDescription("개발자 입니다.")
                .user(user)
                .build();
        this.projectMemberRepository.save(projectMember);
    }

    private void generateUser(int index){
        User user = User.builder()
                .userName("테스터"+index)
                .userId("tester"+index)
                .build();
        this.userRepository.save(user);
    }

}