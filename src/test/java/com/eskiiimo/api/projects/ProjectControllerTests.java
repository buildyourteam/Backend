package com.eskiiimo.api.projects;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.common.TestDescription;
import com.eskiiimo.api.people.Member;
import com.eskiiimo.api.people.MemberRepository;
import com.eskiiimo.api.projects.projectdetail.ProjectDetailDto;
import com.eskiiimo.api.projects.projectsList.ProjectDto;
import com.eskiiimo.api.projects.projectsList.ProjectMemberSet;
import com.eskiiimo.api.projects.projectsList.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class ProjectControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    MemberRepository memberRepository;


    @Test
    @TestDescription("정상적으로 프로젝트를 생성하는 테스트")
    public void createProject() throws Exception {
        ProjectDto project = ProjectDto.builder()
                .projectName("project1")
                .teamName("Team1")
                .endDate(LocalDateTime.of(2020,02,20,11,11))
                .description("Hi this is project1.")
                .projectField(ProjectField.SYSTEM)
                .needMember(new ProjectMemberSet(3,4,4,5))
                .build();

        mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("create-project",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("create-project").description("link to create project"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("projectName").description("projectName"),
                                fieldWithPath("teamName").description("teamName"),
                                fieldWithPath("endDate").description("date time of deadline of new event"),
                                fieldWithPath("description").description("description of new project"),
                                fieldWithPath("projectField").description("field of new project"),
                                fieldWithPath("currentMember").description("current Member of new project"),
                                fieldWithPath("needMember.developer").description("need Developer"),
                                fieldWithPath("needMember.designer").description("need Designer"),
                                fieldWithPath("needMember.planner").description("need Planner"),
                                fieldWithPath("needMember.etc").description("need Etc")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("projectId").description("image file name"),
                                fieldWithPath("projectName").description("projectName"),
                                fieldWithPath("teamName").description("teamName"),
                                fieldWithPath("endDate").description("date time of deadline of new event"),
                                fieldWithPath("description").description("description of new project"),
                                fieldWithPath("dday").description("D-day"),
                                fieldWithPath("status").description("status of new project"),
                                fieldWithPath("projectField").description("field of new project"),
                                fieldWithPath("currentMember").description("current Member of new project"),
                                fieldWithPath("needMember.developer").description("need Developer"),
                                fieldWithPath("needMember.designer").description("need Designer"),
                                fieldWithPath("needMember.planner").description("need Planner"),
                                fieldWithPath("needMember.etc").description("need Etc"),
                                fieldWithPath("_links.self.href").description("Link to Self"),
                                fieldWithPath("_links.create-project.href").description("Link to create project"),
                                fieldWithPath("_links.profile.href").description("Link to Profile")
                        )
                ))

        ;
    }



    @Test
    @TestDescription("프로젝트를 정상적으로 수정")
    public void updateProject() throws Exception {
        // Given
        Project project = this.generateOneProject(1);
        this.joinProjectMember((long)1,1);
        this.joinProjectMember((long)1,2);
        Optional<Project> byId = this.projectRepository.findById(project.getProjectId());
        Project project1 = byId.get();
        ProjectDetailDto projectDto = this.modelMapper.map(project1, ProjectDetailDto.class);
        projectDto.setProjectName("Hi project....");


        // When & Then
        this.mockMvc.perform(put("/api/projects/{project_id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(projectDto)))
                .andDo(print())
                .andExpect(status().isOk());

    }


    @Test
    @TestDescription("프로젝트를 정상적으로 삭제")
    public void deleteProject() throws Exception {
        // Given
        this.generateEvent(1);
        this.generateEvent(2);

        // When & Then
        this.mockMvc.perform(delete("/api/projects/{project_id}", 2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNoContent());

    }


    @Test
    @TestDescription("검색기능사용 없이 전체리스트 조회")
    public void queryProjectsTotal() throws Exception {
        // Given
        IntStream.range(0,30).forEach(i -> {
            this.generateEvent(i);
        });

        // When & Then
        this.mockMvc.perform(get("/api/projects")
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
    @TestDescription("직군별 and 분야별 프로젝트 리스트 조회하기")
    public void queryProjectsOccupationAndField() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/api/projects")
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
                                parameterWithName("page").description("page"),
                                parameterWithName("size").description("number of projects per page"),
                                parameterWithName("sort").description("sort"),
                                parameterWithName("occupation").description("occupation of project"),
                                parameterWithName("field").description("field of project")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(

                                fieldWithPath("_embedded.projectList[].projectId").description("projectId"),
                                fieldWithPath("_embedded.projectList[].projectName").description("projectName"),
                                fieldWithPath("_embedded.projectList[].teamName").description("teamName"),
                                fieldWithPath("_embedded.projectList[].endDate").description("endDate"),
                                fieldWithPath("_embedded.projectList[].description").description("description"),
                                fieldWithPath("_embedded.projectList[].dday").description("dday"),
                                fieldWithPath("_embedded.projectList[].status").description("status"),
                                fieldWithPath("_embedded.projectList[].projectField").description("projectField"),
                                fieldWithPath("_embedded.projectList[].currentMember.developer").description("current Developer"),
                                fieldWithPath("_embedded.projectList[].currentMember.designer").description("current Designer"),
                                fieldWithPath("_embedded.projectList[].currentMember.planner").description("current Planner"),
                                fieldWithPath("_embedded.projectList[].currentMember.etc").description("current Etc Member"),
                                fieldWithPath("_embedded.projectList[].needMember.developer").description("need Developer"),
                                fieldWithPath("_embedded.projectList[].needMember.designer").description("need Designer"),
                                fieldWithPath("_embedded.projectList[].needMember.planner").description("need Planner"),
                                fieldWithPath("_embedded.projectList[].needMember.etc").description("need Etc Member"),
                                fieldWithPath("_embedded.projectList[]._links.self.href").description("Link to Project detail "),
                                fieldWithPath("_links.self.href").description("Link to Self"),
                                fieldWithPath("_links.project-list.href").description("Link to project list"),
                                fieldWithPath("_links.profile.href").description("Link to Profile"),
                                fieldWithPath("page.size").description("number of projects per page"),
                                fieldWithPath("page.totalElements").description("total projects"),
                                fieldWithPath("page.totalPages").description("total pages"),
                                fieldWithPath("page.number").description("number")

                        )
                ))
        ;

    }

    @Test
    @TestDescription("only 직군별 프로젝트 리스트 조회하기")
    public void queryProjectsOccupation() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/api/projects")
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
    @TestDescription("only 분야별로만 프로젝트 리스트 조회하기")
    public void queryProjectsField() throws Exception {
        // Given
        this.generateEvent(0);
        this.generateEvent(1);

        // When & Then
        this.mockMvc.perform(get("/api/projects")
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
    @TestDescription("마감임박한 프로젝트 리스트 조회")
    public void DeadlineProjectList() throws Exception {
        // Given
        this.generateProjectDeadline(0);

        // When & Then
        this.mockMvc.perform(get("/api/projects/deadline")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "projectName,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
//                .andExpect(jsonPath("_embedded.projectList[0]._links.self").exists())
//                .andExpect(jsonPath("_links.self").exists())
//                .andExpect(jsonPath("_links.profile").exists())
//                .andExpect(jsonPath("_links.project-list").exists())
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

                                fieldWithPath("_embedded.projectList[].projectId").description("projectId"),
                                fieldWithPath("_embedded.projectList[].projectName").description("projectName"),
                                fieldWithPath("_embedded.projectList[].teamName").description("teamName"),
                                fieldWithPath("_embedded.projectList[].endDate").description("endDate"),
                                fieldWithPath("_embedded.projectList[].description").description("description"),
                                fieldWithPath("_embedded.projectList[].dday").description("dday"),
                                fieldWithPath("_embedded.projectList[].status").description("status"),
                                fieldWithPath("_embedded.projectList[].projectField").description("projectField"),
                                fieldWithPath("_embedded.projectList[].currentMember.developer").description("current Developer"),
                                fieldWithPath("_embedded.projectList[].currentMember.designer").description("current Designer"),
                                fieldWithPath("_embedded.projectList[].currentMember.planner").description("current Planner"),
                                fieldWithPath("_embedded.projectList[].currentMember.etc").description("current Etc Member"),
                                fieldWithPath("_embedded.projectList[].needMember.developer").description("need Developer"),
                                fieldWithPath("_embedded.projectList[].needMember.designer").description("need Designer"),
                                fieldWithPath("_embedded.projectList[].needMember.planner").description("need Planner"),
                                fieldWithPath("_embedded.projectList[].needMember.etc").description("need Etc Member"),
                                fieldWithPath("_embedded.projectList[]._links.self.href").description("Link to Project detail "),
                                fieldWithPath("_links.self.href").description("Link to Self"),
                                fieldWithPath("_links.deadline-project-list.href").description("Link to deadline project list"),
                                fieldWithPath("_links.profile.href").description("Link to Profile"),
                                fieldWithPath("page.size").description("number of projects per page"),
                                fieldWithPath("page.totalElements").description("total projects"),
                                fieldWithPath("page.totalPages").description("total pages"),
                                fieldWithPath("page.number").description("number")

                        )
                ))
        ;

    }

    private Project generateOneProject(int index) {
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet currentMember = new ProjectMemberSet(2,1,1,2);

        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
                .needMember(need_yes)
//                .projectMembers()
                .status(ProjectStatus.RECRUTING)
                .projectField(ProjectField.APP)
                .build();
        project.update();
        this.projectRepository.save(project);
        return project;

    }


    private void joinProjectMember(Long index,int memberno){
        Optional<Project> optionalProject = this.projectRepository.findById(index);
        Project project = optionalProject.get();
        generateMember(memberno);
        Optional<Member> optionalMember = this.memberRepository.findById((long)memberno);
        Member member =optionalMember.get();
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.DEVELOPER)
                .stack(TechnicalStack.SPRINGBOOT)
                .project(project)
                .selfDescription("개발자 입니다.")
                .member(member)
                .build();
        this.projectMemberRepository.save(projectMember);
    }

    private void generateMember(int index){
        Member member = Member.builder()
                .userName("테스터"+index)
                .userId("tester"+index)
                .build();
        this.memberRepository.save(member);
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
                .status(ProjectStatus.RECRUTING)
                .projectField(ProjectField.APP)
                .build();
        project.update();

        Project project1 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
                .status(ProjectStatus.RECRUTING)
                .projectField(ProjectField.WEB)
                .build();
        project1.update();

        Project project2 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
                .needMember(need_yes)
                .status(ProjectStatus.RECRUTING)
                .projectField(ProjectField.WEB)
                .build();
        project2.update();

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
                .status(ProjectStatus.RECRUTING)
                .build();
        project.update();

        Project project1 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,2,14,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
                .status(ProjectStatus.RECRUTING)
                .build();
        project1.update();

        Project project2 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,03,30,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
                .status(ProjectStatus.RECRUTING)
                .build();
        project2.update();

        this.projectRepository.save(project);
        this.projectRepository.save(project1);
        this.projectRepository.save(project2);

    }

}
