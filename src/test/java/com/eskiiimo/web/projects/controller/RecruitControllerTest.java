package com.eskiiimo.web.projects.controller;

import com.eskiiimo.repository.projects.dto.RecruitDto;
import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.model.Recruit;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
import com.eskiiimo.web.common.BaseControllerTest;
import com.eskiiimo.web.projects.enumtype.*;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.repository.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

@DisplayName("프로젝트 영입하기")
class RecruitControllerTest extends BaseControllerTest {
    @Autowired
    UserRepository profileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MockMvc mockMvc;


    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    RecruitRepository recruitRepository;

    @Test
    @Transactional
    @WithMockUser(username = "tester")
    @DisplayName("프로젝트 영입하기")
    void recruitProject() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(), "tester");
        RecruitDto recruitDto = RecruitDto.builder()
//                .project(project)
                .projectId(project.getProjectId())
                .userName("tester")
                .selfDescription("프로젝트 영입하고 싶습니다.")
                .role(ProjectRole.DEVELOPER)
                .build();
        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit/{projectId}","tester", project.getProjectId())
                .content(objectMapper.writeValueAsString(recruitDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("projectRecruit",
                        pathParameters(
                                parameterWithName("userId").description("유저 아이디"),
                                parameterWithName("projectId").description("프로젝트 아이디")
                        ),
                        requestFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("status").description("상태"),
                                fieldWithPath("projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("projectName").description("영입 제안 프로젝트 이름"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("selfDescription").description("자기소개")
                        )
                ));
    }

    @Test
    @Transactional
    @WithMockUser(username = "tester")
    @DisplayName("나한테 온 프로젝트 영입 제안리스트")
    void getRecruitList() throws Exception{
        Project project = this.generateProject(1);
        Project project1 = this.generateProject(2);
        User user = this.joinProjectLeader(project.getProjectId(), "tester");

        this.generateRecruit(user, project);
        this.generateRecruit(user, project1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit", "tester"))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getRecruits",
                        pathParameters(
                                parameterWithName("userId").description("유저 아이디")
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
    }



    @Test
    @Transactional
    @WithMockUser(username = "tester")
    @DisplayName("나한테 온 영입제안 확인하기(열람시 읽음상태로 전환)")
    void getRecruitProject() throws Exception{
        Project project = this.generateProject(1);
        Project project1 = this.generateProject(2);
        User user = this.joinProjectLeader(project.getProjectId(), "tester");

        this.generateRecruit(user, project);
        this.generateRecruit(user, project1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/profile/{userId}/recruit/{projectId}", "tester", project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("READ"))
                .andDo(print())
                .andDo(document("getRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        ),
                        responseFields(
                                fieldWithPath("userName").description("유저이름"),
                                fieldWithPath("status").description("상태"),
                                fieldWithPath("projectId").description("영입 제안 프로젝트 Id"),
                                fieldWithPath("projectName").description("영입 제안 프로젝트 이름"),
                                fieldWithPath("role").description("지원할 역할"),
                                fieldWithPath("selfDescription").description("자기소개"),
                                fieldWithPath("_links.self.href").description("self 링크"),
                                fieldWithPath("_links.acceptRecruit.href").description("영입 승인하기"),
                                fieldWithPath("_links.rejectRecruit.href").description("영입 거절하기"),
                                fieldWithPath("_links.profile.href").description("Api 명세서")

                        ),
                        links(
                                linkWithRel("self").description("self 링크"),
                                linkWithRel("acceptRecruit").description("영입 승인하기"),
                                linkWithRel("rejectRecruit").description("영입 거절하기"),
                                linkWithRel("profile").description("Api 명세서")
                        )
                ))
        ;

    }

    @Test
    @Transactional
    @WithMockUser(username = "tester")
    @DisplayName("영입제안 승락하기")
    void acceptRecruitProject() throws Exception{
        Project project = this.generateProject(1);
        Project project1 = this.generateProject(2);
        User user = this.joinProjectLeader(project.getProjectId(), "tester");

        this.generateRecruit(user, project);
        this.generateRecruit(user, project1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/profile/{userId}/recruit/{projectId}", "tester", project.getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("acceptRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ));

        this.mockMvc.perform(get("/profile/{userId}/recruit/{projectId}", "tester", project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("ACCEPT"))
        ;
    }

    @Test
    @Transactional
    @WithMockUser(username = "tester")
    @DisplayName("영입제안 거절하기")
    void rejectRecruitProject() throws Exception{
        Project project = this.generateProject(1);
        Project project1 = this.generateProject(2);
        User user = this.joinProjectLeader(project.getProjectId(), "tester");

        this.generateRecruit(user, project);
        this.generateRecruit(user, project1);

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/profile/{userId}/recruit/{projectId}", "tester", project.getProjectId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("rejectRecruit",
                        pathParameters(
                                parameterWithName("projectId").description("프로젝트 아이디"),
                                parameterWithName("userId").description("지원자 아이디")
                        )
                ))
        ;

        this.mockMvc.perform(get("/profile/{userId}/recruit/{projectId}", "tester", project.getProjectId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("REJECT"));
    }

    private Project generateProject(int index) {
        ProjectMemberSet need_yes = new ProjectMemberSet(1,4,6,8);
        ProjectMemberSet currentMember = new ProjectMemberSet(2,1,1,2);
        Project project = Project.builder()
                .projectName("project"+index)
                .teamName("project team"+index*2)
                .endDate(LocalDateTime.of(2020,04,30,23,59))
                .description("need yes 입니다.")
                .currentMember(currentMember)
                .needMember(need_yes)
                .status(Status.RECRUTING)
                .projectField(ProjectField.APP)
//                .leaderId()
//                .questions(questions)
                .build();
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
    private User joinProjectLeader(Long index,String member){
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
        project.setLeaderId(member);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
        return user;
    }
    private void generateRecruit(User user01, Project project01) {
        RecruitDto recruitDto = RecruitDto.builder()
//                .project(project01)
                .projectId(project01.getProjectId())
                .userName("tester")
                .selfDescription("프로젝트 영입하고 싶습니다.")
                .role(ProjectRole.DEVELOPER)
                .build();
        Recruit recruit = recruitDto.toEntity(user01, project01);
        this.recruitRepository.save(recruit);
    }
}