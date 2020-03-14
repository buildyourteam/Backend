package com.eskiiimo.api.projects.detail;

import com.eskiiimo.api.common.BaseControllerTest;
import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.apply.entity.ProjectApplyQuestion;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import com.eskiiimo.api.user.recruit.RecruitDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("프로젝트 상세 페이지")
class ProjectDetailControllerTest extends BaseControllerTest {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("프로젝트 상세 페이지 확인하기")
    @Transactional
    void getProjectDetail() throws Exception {
        // Given
        Project project = this.generateOneProject(2);
        this.joinProjectMember(project.getProjectId(),1);
        this.joinProjectMember(project.getProjectId(),2);
        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}",project.getProjectId()))
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
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("dday").description("마감일까지 남은 일"),
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
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("_links.self.href").description("Self 링크"),
                                fieldWithPath("_links.apply.href").description("프로젝트 지원 링크"),
                                fieldWithPath("_links.profile.href").description("API 명세서")


                        )
                 ))
        ;
    }


    @Test
    @Transactional
    @DisplayName("내가 보낸 영입제안 리스트 확인하기")
    @WithMockUser(username = "tester")
    public void getRecruits() throws Exception{
        Project project = this.generateProject(1);
        Long project_id = project.getProjectId();
        this.joinProjectLeader(project_id,"tester");


        User user01 = User.builder()
                .userName("유저01")
                .userId("user01")
                .password("testpassword")
                .build();
        this.userRepository.save(user01);
        User user02 = User.builder()
                .userName("유저02")
                .userId("user02")
                .password("testpassword2")
                .build();
        this.userRepository.save(user02);

        RecruitDto recruitDto1 = RecruitDto.builder()
//                .project(project)
                .projectId(project_id)
                .userName("user01")
                .selfDescription("프로젝트 영입하고 싶습니다.")
                .role(ProjectRole.DEVELOPER)
                .build();
        RecruitDto recruitDto2 = RecruitDto.builder()
//                .project(project)
                .projectId(project_id)
                .userName("user02")
                .selfDescription("프로젝트 영입하고 싶습니다.")
                .role(ProjectRole.DEVELOPER)
                .build();
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit/{projectId}","user01", project_id)
                .content(objectMapper.writeValueAsString(recruitDto1))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit/{projectId}","user02", project_id)
                .content(objectMapper.writeValueAsString(recruitDto2))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print());

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/recruits", project_id))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getSendRecruits",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.recruitDtoList[].userName").description("유저이름"),
                                fieldWithPath("_embedded.recruitDtoList[].selfDescription").description("자기소개"),
                                fieldWithPath("_embedded.recruitDtoList[].role").description("지원할 역할"),
                                fieldWithPath("_embedded.recruitDtoList[].status").description("상태"),
                                fieldWithPath("_embedded.recruitDtoList[].projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("_embedded.recruitDtoList[].projectName").description("영입 제안 프로젝트 이름"),
                                fieldWithPath("_embedded.recruitDtoList[]._links.self.href").description("self 링크"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ));
        ;
    }



    @Test
    @WithMockUser(username="projectLeader")
    @Transactional
    @DisplayName("프로젝트 생성하기")
    public void createProject() throws Exception {
        this.generateUser("projectLeader");
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("question1").build());
        questions.add(ProjectApplyQuestion.builder().question("question2").build());
        ProjectDetailDto project = ProjectDetailDto.builder()
                .projectName("project1")
                .teamName("Team1")
                .endDate(LocalDateTime.of(2020,02,20,11,11))
                .description("Hi this is project1.")
                .needMember(new ProjectMemberSet(3,4,4,5))
                .projectField(ProjectField.WEB)
                .applyCanFile(Boolean.TRUE)
                .questions(questions)
                .build();

        mockMvc.perform(post("/projects")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(project)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("create-project",
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("createdProject").description("생성된 프로젝트 링크"),
                                linkWithRel("profile").description("API 명세서")
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
                                fieldWithPath("status").description("프로젝트 상태(모집중)"),
                                fieldWithPath("dday").description("마감일까지 남은 일"),
                                fieldWithPath("memberList").description("프로젝트에 참가하는 멤버 리스트"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("currentMember").description("팀원 현황"),
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        )
                ))
        ;
        mockMvc.perform(get("/projects/12"))
        .andDo(print());
    }



    @Test
    @WithMockUser(username="testuser")
    @DisplayName("프로젝트 수정하기")
    @Transactional
    public void updateProject() throws Exception {
        // Given
        Project project = this.generateOneProject(1);
        this.joinProjectLeader(project.getProjectId(),"testuser");
        this.joinProjectMember(project.getProjectId(),2);
        Project project1 = this.projectRepository.findById(project.getProjectId()).get();
        UpdateDto updateDto = UpdateDto.builder()
                .projectName(project1.getProjectName())
                .teamName(project1.getTeamName())
                .endDate(project1.getEndDate())
                .description(project1.getDescription())
                .status(project1.getStatus())
                .needMember(project1.getNeedMember())
                .questions(project1.getQuestions())
                .applyCanFile(project1.getApplyCanFile())
                .projectField(project1.getProjectField())
                .build();
        updateDto.setProjectName("Hi project....");


        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{project_id}", project.getProjectId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(updateDto)))
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
                                fieldWithPath("status").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
                        ),
                        responseFields(
                                fieldWithPath("projectName").description("프로젝트 이름"),
                                fieldWithPath("teamName").description("팀명"),
                                fieldWithPath("endDate").description("마감일"),
                                fieldWithPath("description").description("프로젝트에 대한 설명"),
                                fieldWithPath("dday").description("마감일까지 남은 일"),
                                fieldWithPath("status").description("프로젝트 상태(모집중, 진행중, 마감)"),
                                fieldWithPath("memberList").description("프로젝트에 참가하는 멤버 리스트"),
                                fieldWithPath("projectField").description("프로젝트 분야(앱, 웹, AI 등등.."),
                                fieldWithPath("applyCanFile").description("지원서에 파일업로드 가능여부"),
                                fieldWithPath("questions[]").description("프로젝트 지원서용 질문"),
                                fieldWithPath("currentMember.developer").description("현재 개발자 수"),
                                fieldWithPath("currentMember.designer").description("현재 디자이너 수"),
                                fieldWithPath("currentMember.planner").description("현재 기획자 수"),
                                fieldWithPath("currentMember.etc").description("현재 기타 수"),
                                fieldWithPath("needMember.developer").description("필요한 개발자 수"),
                                fieldWithPath("needMember.designer").description("필요한 디자이너 수"),
                                fieldWithPath("needMember.planner").description("필요한 기획자 수"),
                                fieldWithPath("needMember.etc").description("그 외 필요한 인원수"),
                                fieldWithPath("memberList[].userName").description("프로젝트 팀원의 이름"),
                                fieldWithPath("memberList[].role").description("프로젝트 팀원의 역할"),
                                fieldWithPath("memberList[].stack").description("프로젝트 팀원의 기술스택"),
                                fieldWithPath("memberList[].level").description("프로젝트 팀원의 레벨"),
                                fieldWithPath("memberList[]._links.self.href").description("프로젝트 팀원의 프로필"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        )
                ))
        ;
    }


    @Test
    @WithMockUser(username="tester")
    @Transactional
    @DisplayName("프로젝트 삭제하기")
    public void deleteProject() throws Exception {
        // Given
        Project project = this.generateOneProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");

        // When & Then
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{project_id}", project.getProjectId())
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

    private Project generateOneProject(int index) {
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet currentMember = new ProjectMemberSet(2,1,1,2);
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("question1").build());
        questions.add(ProjectApplyQuestion.builder().question("question2").build());
        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
                .needMember(need_yes)
                .status(Status.RECRUTING)
                .projectField(ProjectField.APP)
                .questions(questions)
                .dday(ChronoUnit.DAYS.between(LocalDateTime.now(), LocalDateTime.of(2020,04,30,23,59)))
                .applyCanFile(Boolean.TRUE)
                .build();
        this.projectRepository.save(project);
        return project;

    }

    private void joinProjectMember(Long index,int memberno){
        Optional<Project> optionalProject = this.projectRepository.findById(index);
        Project project = optionalProject.get();
        User user = generateUser(memberno);
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.DEVELOPER)
                .stack(TechnicalStack.SPRINGBOOT)
                .project(project)
                .selfDescription("개발자 입니다.")
                .user(user)
                .build();
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }

    private Project generateProject(int index) {
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet currentMember = new ProjectMemberSet(2,1,1,2);
//        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
//        questions.add(ProjectApplyQuestion.builder().question("1번 질문").build());
//        questions.add(ProjectApplyQuestion.builder().question("2번 질문").build());
//        questions.add(ProjectApplyQuestion.builder().question("3번 질문").build());
        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
//                .leaderId("tester")
                .needMember(need_yes)
                .status(Status.RECRUTING)
                .projectField(ProjectField.APP)
//                .questions(questions)
                .build();
        this.projectRepository.save(project);
        return project;

    }

    private void joinProjectLeader(Long index,String memberId){
        Optional<Project> optionalProject = this.projectRepository.findById(index);
        Project project = optionalProject.get();
        User user = generateUser(memberId);
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.LEADER)
                .stack(TechnicalStack.SPRINGBOOT)
                .project(project)
                .user(user)
                .build();
        project.getProjectMembers().add(projectMember);
        project.setLeaderId(memberId);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }

    private User generateUser(int index){
        User user = User.builder()
                .userName("테스터"+index)
                .userId("tester"+index)
                .password("testpassword")
                .build();
       return this.userRepository.save(user);
    }
    private User generateUser(String tester){
        User user = User.builder()
                .userName("테스터")
                .userId(tester)
                .userEmail("UserEmail")
                .password("pasword")
                .build();
        return this.userRepository.save(user);
    }

}