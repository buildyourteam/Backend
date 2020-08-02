package com.eskiiimo.web.common.TestFactory.project;

import com.eskiiimo.repository.projects.model.*;
import com.eskiiimo.repository.projects.repository.ProjectApplyRepository;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.projects.repository.RecruitRepository;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.TestFactory.user.TestUserFactory;
import com.eskiiimo.web.projects.enumtype.*;
import com.eskiiimo.web.projects.request.ProjectApplyRequest;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import com.eskiiimo.web.projects.request.RecruitProjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestProjectFactory {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

    @Autowired
    TestUserFactory testUserFactory;

    @Autowired
    ProjectApplyRepository projectApplyRepository;

    @Autowired
    RecruitRepository recruitRepository;

    /*
    프로젝트를 생성하고, 해당 프로젝트 팀장을 연결
     */
    public Project generateProject(int index, User user, State status) {
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(ProjectApplyQuestion.builder().question("question1").build());
        questions.add(ProjectApplyQuestion.builder().question("question2").build());

        Project project = Project.builder()
                .projectName("project" + index)
                .teamName("project team" + index * 2)
                .endDate(LocalDateTime.now().plusDays(1))
                .introduction("need yes 입니다.")
                .currentMember(new ProjectMemberSet(2, 1, 1, 2))
                .needMember(new ProjectMemberSet(1, 4, 6, 8))
                .state(status)
                .projectField(ProjectField.APP)
                .leaderId(user.getUserId())
                .questions(questions)
                .build();
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .hide(Boolean.FALSE)
                .role(ProjectRole.LEADER)
                .introduction("프로젝트 팀장 입니다.")
                .build();
        projectMember.joinProject(project);

        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
        return project;
    }

    /*
    프로젝트 필터(분야,직군)별 검색기능 테스트를 위한 프로젝트 생성
    */
    public Project generateProject(int index, ProjectField projectField, Boolean is_need) {

        ProjectMemberSet need_zero = new ProjectMemberSet(0, 0, 0, 0);
        ProjectMemberSet need_yes = new ProjectMemberSet(1, 4, 6, 8);
        ProjectMemberSet needMember;
        if (is_need.equals(true)) {
            needMember = need_yes;
        } else {
            needMember = need_zero;
        }
        Project project = Project.builder()
                .projectName("project" + index)
                .teamName("project team" + index * 2)
                .endDate(LocalDateTime.now().plusDays(1))
                .introduction("need yes 입니다.")
                .currentMember(new ProjectMemberSet(2, 1, 1, 2))
                .needMember(needMember)
                .projectField(projectField)
                .build();

        this.projectRepository.save(project);
        return project;
    }

    public Project generateMyProject(int index) {
        User leader = testUserFactory.generateUser(index);
        Project project = generateProject(0, leader, State.RECRUTING);
        return project;
    }

    /*
    프로젝트 팀원 매칭
     */
    public ProjectMember generateProjectMember(User user, Project project, Boolean hide) {
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .role(ProjectRole.DEVELOPER)
                .introduction("프로젝트 팀원 입니다.")
                .hide(hide)
                .build();
        project.getProjectMembers().add(projectMember);

        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
        return projectMember;
    }

    public ProjectApply generateApply(Project project, User user) {
        ProjectApplyRequest projectApplyRequest = generateProjectApplyRequest();

        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for (String answer : projectApplyRequest.getAnswers())
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());

        ProjectApply projectApply = ProjectApply.builder()
                .answers(answers)
                .introduction(projectApplyRequest.getIntroduction())
                .role(projectApplyRequest.getRole())
                .state(ProjectApplyState.UNREAD)
                .user(user)
                .build();
        project.addApply(projectApply);

        this.projectApplyRepository.save(projectApply);
        this.projectRepository.save(project);
        return projectApply;
    }

    public Project generateProjectApplies(int index) {
        Project project = generateMyProject(0);
        IntStream.range(1, index + 1).forEach(i -> {
            generateApply(project, testUserFactory.generateUser(i));
        });
        return project;
    }

    public Recruit generateRecruit(User user01, Project project01) {
        RecruitProjectRequest recruitProjectRequest = generateRecruitRequest(project01.getProjectId(), user01);
        Recruit recruit = Recruit.builder()
                .role(recruitProjectRequest.getRole())
                .introduction(recruitProjectRequest.getIntroduction())
                .user(user01)
                .project(project01)
                .state(RecruitState.UNREAD)
                .projectId(project01.getProjectId())
                .projectName(project01.getProjectName())
                .build();
        this.recruitRepository.save(recruit);
        return recruit;
    }

    public List<Project> generateProjectRecruits(int index, User user) {
        List<Project> projects = new ArrayList<>();
        IntStream.range(0, index).forEach(i -> {
            Project project = generateProject(i, ProjectField.WEB, Boolean.TRUE);
            generateRecruit(user, project);
            projects.add(project);
        });
        return projects;
    }

    public ProjectDetailRequest generateProjectDetailRequest(Project myProject) {
        ProjectDetailRequest projectDetailRequest = ProjectDetailRequest.builder()
                .projectName(myProject.getProjectName())
                .teamName(myProject.getTeamName())
                .endDate(myProject.getEndDate())
                .introduction(myProject.getIntroduction())
                .needMember(myProject.getNeedMember())
                .projectField(myProject.getProjectField())
                .applyCanFile(myProject.getApplyCanFile())
                .questions(myProject.getQuestions())
                .build();
        return projectDetailRequest;
    }

    public ProfileDto generateProfileDto() {
        List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
        stacks.add(TechnicalStack.DJANGO);
        ProfileDto profileDto = ProfileDto.builder()
                .area("서울시 구로구")
                .contact("010-9876-5432")
                .introduction("프로필 업데이트 하기")
                .role(ProjectRole.LEADER)
                .stacks(stacks)
                .userName("회원 01")
                .grade((long) 100)
                .build();

        return profileDto;
    }

    public ProjectDetailRequest generateProjectUpdateDto(Project project) {
        ProjectDetailRequest projectDetailRequest = ProjectDetailRequest.builder()
                .projectName(project.getProjectName())
                .teamName(project.getTeamName())
                .endDate(project.getEndDate())
                .introduction(project.getIntroduction())
                .state(project.getState())
                .needMember(project.getNeedMember())
                .questions(project.getQuestions())
                .applyCanFile(project.getApplyCanFile())
                .projectField(project.getProjectField())
                .build();
        return projectDetailRequest;
    }

    public RecruitProjectRequest generateRecruitRequest(Long projectId, User user) {
        RecruitProjectRequest recruitProjectRequest = RecruitProjectRequest.builder()
                .projectId(projectId)
                .introduction("플젝에 영입하고 싶어요")
                .role(user.getRole())
                .build();
        return recruitProjectRequest;
    }

    public ProjectApplyRequest generateProjectApplyRequest() {
        List<String> answers = new ArrayList<String>();
        answers.add("1번 응답");
        answers.add("2번 응답");
        answers.add("3번 응답");
        ProjectApplyRequest projectApplyRequest = ProjectApplyRequest.builder()
                .role(ProjectRole.DEVELOPER)
                .introduction("안녕하세요? 저는 그냥 개발자입니다.")
                .answers(answers)
                .build();
        return projectApplyRequest;
    }


}
