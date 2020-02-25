package com.eskiiimo.api.projects.projectlist;

import com.eskiiimo.api.common.RestDocsConfiguration;
import com.eskiiimo.api.common.TestDescription;
import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.projectdetail.ProjectDetailDto;
import com.eskiiimo.api.projects.projectsList.ProjectListDto;
import com.eskiiimo.api.projects.ProjectMemberSet;
import com.eskiiimo.api.projects.ProjectRepository;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
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
@AutoConfigureRestDocs(uriScheme= "https",uriHost = "api.eskiiimo.com" ,uriPort = 443)
@Import(RestDocsConfiguration.class)
public class ProjectListControllerTests {

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
    UserRepository userRepository;


    @Test
    @TestDescription("정상적으로 프로젝트를 생성하는 테스트")
    public void createProject() throws Exception {
        ProjectListDto project = ProjectListDto.builder()
                .projectName("project1")
                .teamName("Team1")
                .endDate(LocalDateTime.of(2020,02,20,11,11))
                .description("Hi this is project1.")
                .projectField(ProjectField.SYSTEM)
                .needMember(new ProjectMemberSet(3,4,4,5))
                .build();

        mockMvc.perform(post("/projects")
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
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("description").description("프로젝트에 대한 설명"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("currentMember").description("팀원 현황"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("projectId").description("프로젝트 아이디 (=이미지 파일 이름)"),
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("description").description("프로젝트에 대한 설명"),
                                fieldWithPath("dday").description("마감일까지 남은 일"),
                                fieldWithPath("status").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("currentMember.developer").description("팀원 현황"),
                                fieldWithPath("currentMember.designer").description("팀원 현황"),
                                fieldWithPath("currentMember.planner").description("팀원 현황"),
                                fieldWithPath("currentMember.etc").description("팀원 현황"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.create-project.href").description("프로젝트 생성 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
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
        ProjectListDto projectDto = this.modelMapper.map(project1, ProjectListDto.class);
        projectDto.setProjectName("Hi project....");


        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{project_id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(projectDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update-project",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        pathParameters(
                                parameterWithName("project_id").description("Project id")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("description").description("프로젝트에 대한 설명"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("projectId").description("프로젝트 아이디 (=이미지 파일 이름)"),
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("description").description("프로젝트에 대한 설명"),
                                fieldWithPath("dday").description("마감일까지 남은 일"),
                                fieldWithPath("status").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
                ;

    }


    @Test
    @TestDescription("프로젝트를 정상적으로 삭제")
    public void deleteProject() throws Exception {
        // Given
        this.generateEvent(1);
        this.generateEvent(2);

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{project_id}", (long)2)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-project",
                        pathParameters(
                                parameterWithName("project_id").description("Project id")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        )
                ))

        ;

    }


    @Test
    @TestDescription("검색기능사용 없이 전체리스트 조회")
    public void queryProjectsTotal() throws Exception {
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
    @TestDescription("직군별 and 분야별 프로젝트 리스트 조회하기")
    public void queryProjectsOccupationAndField() throws Exception {
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
                                fieldWithPath("_embedded.projectList[].currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("_embedded.projectList[].needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.etc").description("그 외 필요한 인원 수"),
                                fieldWithPath("_embedded.projectList[]._links.self.href").description("프로젝트 상세페이지로 가는 링크"),
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
    @TestDescription("only 직군별 프로젝트 리스트 조회하기")
    public void queryProjectsOccupation() throws Exception {
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
    @TestDescription("only 분야별로만 프로젝트 리스트 조회하기")
    public void queryProjectsField() throws Exception {
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
    @TestDescription("마감임박한 프로젝트 리스트 조회")
    public void DeadlineProjectList() throws Exception {
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

                                fieldWithPath("_embedded.projectList[].projectId").description("프로젝트 아이디 (=이미지 파일 이름)"),
                                fieldWithPath("_embedded.projectList[].projectName").description("프로젝트 이름"),
                                fieldWithPath("_embedded.projectList[].teamName").description("팀명"),
                                fieldWithPath("_embedded.projectList[].endDate").description("마감일"),
                                fieldWithPath("_embedded.projectList[].description").description("프로젝트에 대한 설명"),
                                fieldWithPath("_embedded.projectList[].dday").description("마감일까지 남은 일"),
                                fieldWithPath("_embedded.projectList[].status").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("_embedded.projectList[].projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("_embedded.projectList[].currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("_embedded.projectList[].currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("_embedded.projectList[].needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("_embedded.projectList[].needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("_embedded.projectList[].needMember.etc").description("그 외 필요한 인원 수"),
                                fieldWithPath("_embedded.projectList[]._links.self.href").description("프로젝트 상세페이지로 가는 링크"),
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
        generateUser(memberno);
        Optional<User> optionalUser = this.userRepository.findById((long)memberno);
       User user =optionalUser.get();
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
//                .status(ProjectStatus.RECRUTING)
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
//                .status(ProjectStatus.RECRUTING)
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
//                .status(ProjectStatus.RECRUTING)
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
//                .status(ProjectStatus.RECRUTING)
                .build();
        project.update();

        Project project1 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,2,14,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
//                .status(ProjectStatus.RECRUTING)
                .build();
        project1.update();

        Project project2 = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,03,30,23,59))
                .description("need zero 입니다.")
                .currentMember(currentMember)
                .needMember(need_zero)
//                .status(ProjectStatus.RECRUTING)
                .build();
        project2.update();

        this.projectRepository.save(project);
        this.projectRepository.save(project1);
        this.projectRepository.save(project2);

    }

}
