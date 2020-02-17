package com.eskiiimo.api.projects.projectdetail;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.projectsList.ProjectMemberSet;
import com.eskiiimo.api.projects.projectsList.ProjectRepository;
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

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

                                fieldWithPath("projectName").description("projectName"),
                                fieldWithPath("teamName").description("teamName"),
                                fieldWithPath("endDate").description("endDate"),
                                fieldWithPath("description").description("description"),
                                fieldWithPath("status").description("status"),
                                fieldWithPath("current.developer").description("current Developer"),
                                fieldWithPath("current.designer").description("current Designer"),
                                fieldWithPath("current.planner").description("current Planner"),
                                fieldWithPath("current.etc").description("current Etc Member"),
                                fieldWithPath("needMembers.developer").description("need Developer"),
                                fieldWithPath("needMembers.designer").description("need Designer"),
                                fieldWithPath("needMembers.planner").description("need Planner"),
                                fieldWithPath("needMembers.etc").description("need Etc Member"),
                                fieldWithPath("memberList[].userName").description("Project Member's name"),
                                fieldWithPath("memberList[].role").description("Project Member's role"),
                                fieldWithPath("memberList[].stack").description("Project Member's stack"),
                                fieldWithPath("memberList[].level").description("Project Member's level"),
                                fieldWithPath("memberList[]._links.self.href").description("Link to Project Member's profile "),
                                fieldWithPath("_links.self.href").description("Link to Self"),
                                fieldWithPath("_links.apply.href").description("Link to apply"),
                                fieldWithPath("_links.profile.href").description("Link to Profile")


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
                .current(current)
                .needMembers(need_yes)
                .status(ProjectStatus.RECRUTING)
                .build();

        this.projectRepository.save(project);

    }


    private void joinProjectMember(Long index,int memberno){
        Optional<Project> optionalProject = this.projectRepository.findById(index);
        Project project = optionalProject.get();
        generateMember(memberno);
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

    private void generateMember(int index){
        User user = User.builder()
                .userName("테스터"+index)
                .userId("tester"+index)
                .build();
        this.userRepository.save(user);
    }

}