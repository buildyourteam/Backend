package com.eskiiimo.api.user.projectrecruit;

import com.eskiiimo.api.common.BaseControllerTest;
import com.eskiiimo.api.projects.*;
import com.eskiiimo.api.user.User;
import com.eskiiimo.api.user.UserRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("프로젝트 영입하기")
class ProjectRecruitControllerTest extends BaseControllerTest {
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
    ProjectRecruitRepository projectRecruitRepository;

    @Test
    @Transactional
    @WithMockUser(username = "tester")
    @DisplayName("프로젝트 영입하기")
    void recruitProject() throws Exception {
        Project project = this.generateProject(1);
        this.joinProjectLeader(project.getProjectId(), "tester");
        ProjectRecruitDto projectRecruitDto = ProjectRecruitDto.builder()
//                .project(project)
                .projectId(project.getProjectId())
                .userName("tester")
                .selfDescription("프로젝트 영입하고 싶습니다.")
                .role(ProjectRole.DEVELOPER)
                .build();

        this.mockMvc.perform(RestDocumentationRequestBuilders.post("/profile/{userId}/recruit/{projectId}","tester", project.getProjectId())
                .content(objectMapper.writeValueAsString(projectRecruitDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
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
                .andDo(print());
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
                .andDo(print());

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
                .andDo(print());

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
                .andDo(print());

        this.mockMvc.perform(get("/profile/{userId}/recruit/{projectId}", "tester", project.getProjectId()))
                .andExpect(status().isNotFound());
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
//                .questions(questions)
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
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
        return user;
    }
    private void generateRecruit(User user01, Project project01) {
        ProjectRecruitDto projectRecruitDto = ProjectRecruitDto.builder()
//                .project(project01)
                .projectId(project01.getProjectId())
                .userName("tester")
                .selfDescription("프로젝트 영입하고 싶습니다.")
                .role(ProjectRole.DEVELOPER)
                .build();
        ProjectRecruit projectRecruit=projectRecruitDto.toEntity(user01, project01);
        this.projectRecruitRepository.save(projectRecruit);
    }
}