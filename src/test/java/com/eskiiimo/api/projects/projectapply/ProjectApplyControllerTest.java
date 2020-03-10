package com.eskiiimo.api.projects.projectapply;

import com.eskiiimo.api.common.BaseControllerTest;
import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApply;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApplyAnswer;
import com.eskiiimo.api.projects.projectapply.entity.ProjectApplyQuestion;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("프로젝트 지원하기")
class ProjectApplyControllerTest extends BaseControllerTest {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectApplyRepository projectApplyRepository;
    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Test
    @Transactional
    @DisplayName("프로젝트 지원하기")
    @WithMockUser(username = "tester")
    void applyProject() throws Exception{
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        answers.add(ProjectApplyAnswer.builder().answer("1번 응답").build());
        answers.add(ProjectApplyAnswer.builder().answer("2번 응답").build());
        answers.add(ProjectApplyAnswer.builder().answer("3번 응답").build());
        ProjectApplyDto projectApplyDto = ProjectApplyDto.builder()
                .role(ProjectRole.DEVELOPER)
                .selfDescription("안녕하세요? 저는 그냥 개발자입니다.")
                .answers(answers)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/projects/{projectId}/apply",project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("applyProject",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("유저이름(NULL)"),
                                fieldWithPath("status").description("상태(NULL)"),
                                fieldWithPath("questions").description("지원서 질문(NULL)"),
                                fieldWithPath("answers").description("지원서 응답"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("selfDescription").description("자기소개")
                        )
                        ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"tester"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userName").value("tester"))
                .andExpect(jsonPath("role").value("DEVELOPER"))
                .andExpect(jsonPath("selfDescription").value("안녕하세요? 저는 그냥 개발자입니다."))
                .andExpect(jsonPath("answers[0]").value("1번 응답"))
                .andExpect(jsonPath("answers[1]").value("2번 응답"))
                .andExpect(jsonPath("answers[2]").value("3번 응답"))
        ;

    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 수정하기")
    @WithMockUser(username = "tester")
    void updateApply() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");
        this.generateApply(project.getProjectId(), this.userRepository.findByUserId("tester").get());
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        answers.add(ProjectApplyAnswer.builder().answer("1번 응답").build());
        answers.add(ProjectApplyAnswer.builder().answer("2번 응답").build());
        answers.add(ProjectApplyAnswer.builder().answer("3번 응답").build());
        ProjectApplyDto projectApplyDto = ProjectApplyDto.builder()
                .role(ProjectRole.DESIGNER)
                .selfDescription("안녕하세요? 저는 그냥 개발자가 아니라 디자이너입니다.")
                .answers(answers)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply",project.getProjectId())
                .content(objectMapper.writeValueAsString(projectApplyDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("updateApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("status").description("상태(NULL)"),
                                fieldWithPath("questions").description("지원서 질문"),
                                fieldWithPath("answers").description("지원서 응답"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("selfDescription").description("자기소개")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"tester"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("selfDescription").value("안녕하세요? 저는 그냥 개발자가 아니라 디자이너입니다."))
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 확인하기")
    @WithMockUser(username = "tester")
    void getApplicants() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");

        this.generateApply(project.getProjectId(), this.generateUser("testApplicant1"));
        this.generateApply(project.getProjectId(), this.generateUser("testApplicant2"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getApplicants",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.projectApplicantDtoList[].userId").description("유저Id"),
                                fieldWithPath("_embedded.projectApplicantDtoList[].userName").description("유저이름"),
                                fieldWithPath("_embedded.projectApplicantDtoList[]status").description("상태"),
                                fieldWithPath("_embedded.projectApplicantDtoList[]role").description("지원할 역할"),
                                fieldWithPath("_embedded.projectApplicantDtoList[]._links.self.href").description("해당 지원자의 지원서 링크"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;
    }
    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 확인하기_지원자가 없을때")
    @WithMockUser(username = "tester")
    void getApplicantsNoApply() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");

        this.mockMvc.perform(get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isNotFound())
                .andDo(print())
        ;
    }
    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 확인하기_권한없는 사용자")
    @WithMockUser(username = "testers")
    void getApplicantsWrongUser() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");

        this.mockMvc.perform(get("/projects/{projectId}/apply", project.getProjectId()))
                .andExpect(status().isForbidden())
                .andDo(print())
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원서 확인하기")
    @WithMockUser(username = "tester")
    void getApply() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");
        this.generateApply(project.getProjectId(), this.generateUser("testApplicant"));

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"testApplicant"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("status").description("상태"),
                                fieldWithPath("questions").description("질문"),
                                fieldWithPath("answers").description("응답"),
                                fieldWithPath("selfDescription").description("자기소개"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.acceptApply.href").description("지원서 승인하기"),
                                fieldWithPath("_links.rejectApply.href").description("지원서 거절하기"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")
                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("acceptApply").description("지원서 승인하기"),
                                linkWithRel("rejectApply").description("지원서 거절하기"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;

    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 수락하기")
    @WithMockUser(username = "tester")
    void acceptMember() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");
        this.generateApply(project.getProjectId(), this.generateUser("testApplicant"));
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/projects/{projectId}/apply/{userId}", project.getProjectId(),"testApplicant"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("acceptApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"testApplicant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("ACCEPT"))
        ;
        this.mockMvc.perform(get("/projects/{projectId}", project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("memberList[1].userName").value("테스터"))
                .andExpect(jsonPath("memberList[1].role").value("DEVELOPER"))
        ;
    }

    @Test
    @Transactional
    @DisplayName("프로젝트 지원자 거절하기")
    @WithMockUser(username = "tester")
    void rejectMember() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(),"tester");
        this.generateApply(project.getProjectId(), this.generateUser("testApplicant"));
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/projects/{projectId}/apply/{userId}", project.getProjectId(),"testApplicant"))
                .andExpect(status().isOk())
                .andDo(document("rejectApply",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;
        this.mockMvc.perform(get("/projects/{projectId}/apply/{userId}", project.getProjectId(),"testApplicant"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("REJECT"))
        ;
    }

    private Project generateProject(int index) {
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet currentMember = new ProjectMemberSet(2,1,1,2);
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("1번 질문").build());
        questions.add(ProjectApplyQuestion.builder().question("2번 질문").build());
        questions.add(ProjectApplyQuestion.builder().question("3번 질문").build());
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
                .build();
        project.update();
        this.projectRepository.save(project);
        return project;

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
    private void joinProjectLeader(Long index,String member){
        Optional<Project> optionalProject = this.projectRepository.findById(index);
        Project project = optionalProject.get();
        User user = generateUser(member);
        ProjectMember projectMember = ProjectMember.builder()
                .role(ProjectRole.LEADER)
                .stack(TechnicalStack.SPRINGBOOT)
                .project(project)
                .user(user)
                .build();
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
    }

    private ProjectApply generateApply(Long projectId,User user){
        Optional<Project> optionalProject= this.projectRepository.findById(projectId);
        Project project = optionalProject.get();
        List<ProjectApply> applies  = project.getApplies();
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        answers.add(ProjectApplyAnswer.builder().answer("1번 응답").build());
        answers.add(ProjectApplyAnswer.builder().answer("2번 응답").build());
        answers.add(ProjectApplyAnswer.builder().answer("3번 응답").build());
        ProjectApplyDto projectApplyDto = ProjectApplyDto.builder()
                .role(ProjectRole.DEVELOPER)
                .selfDescription("안녕하세요? 저는 그냥 개발자입니다.")
                .answers(answers)
                .build();
        ProjectApply projectApply =projectApplyDto.toEntity(user);
        List<ProjectApply> projectApplies;
        applies.add(projectApply);
        project.setApplies(applies);
        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
        return projectApply;
    }


}