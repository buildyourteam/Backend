package com.eskiiimo.web.common.TestFactory.project;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.common.TestFactory.user.TestUserFactory;
import com.eskiiimo.web.projects.enumtype.*;
import com.eskiiimo.web.projects.request.ProjectApplyRequest;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import com.eskiiimo.web.projects.request.RecruitProjectRequest;
import com.eskiiimo.web.projects.service.ProjectApplyService;
import com.eskiiimo.web.projects.service.ProjectDetailService;
import com.eskiiimo.web.projects.service.RecruitService;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import com.eskiiimo.web.user.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class TestProjectFactory {

    @Autowired
    TestUserFactory testUserFactory;

    @Autowired
    ProjectDetailService projectDetailService;

    @Autowired
    ProjectApplyService projectApplyService;

    @Autowired
    RecruitService recruitService;

    @Autowired
    ProfileService profileService;

    /*
    프로젝트를 생성하고, 해당 프로젝트 팀장을 연결
     */
    public Project generateProject(int index, User user, State status) {
        ProjectDetailRequest createProjectRequest = generateProjectDetailRequest(index, status, ProjectField.WEB, true);

        return projectDetailService.storeProject(createProjectRequest, user.getUserId());
    }

    /*
    프로젝트 필터(분야,직군)별 검색기능 테스트를 위한 프로젝트 생성
    */
    private Project generateProject(int index, ProjectField projectField, Boolean is_need) {
        User leader = testUserFactory.generateUser(index);
        ProjectDetailRequest createProjectRequest = generateProjectDetailRequest(index, State.RECRUTING, projectField, is_need);

        return projectDetailService.storeProject(createProjectRequest, leader.getUserId());
    }

    @Transactional
    public void generateProjects() {
        generateProject(0, ProjectField.AI, false);
        generateProject(1, ProjectField.APP, true);
        generateProject(2, ProjectField.WEB, true);
        generateProject(3, ProjectField.WEB, true);
        generateProject(4, ProjectField.WEB, true);
        generateProject(5, ProjectField.WEB, true);

    }

    public Project generateMyProject(int index) {
        User leader = testUserFactory.generateUser(index);

        return generateProject(0, leader, State.RECRUTING);
    }

    /*
    프로젝트 팀원 매칭
     */
    public void generateProjectMember(User user, Project project, Boolean hide) {
        generateApply(project, user);

        projectApplyService.acceptApply(project.getProjectId(), user.getUserId(), project.getLeaderId());
        if (hide)
            profileService.hideProject(user.getUserId(), user.getUserId(), project.getProjectId());
    }

    public void generateApply(Project project, User user) {
        ProjectApplyRequest projectApplyRequest = generateProjectApplyRequest();

        projectApplyService.applyProject(project.getProjectId(), projectApplyRequest, user.getUserId());
    }

    public Project generateProjectApplies(int index) {
        Project project = generateMyProject(0);
        IntStream.range(1, index + 1).forEach(i -> {
            generateApply(project, testUserFactory.generateUser(i));
        });
        return project;
    }

    public void generateRecruit(User user, Project project) {
        RecruitProjectRequest recruitProjectRequest = generateRecruitRequest(project.getProjectId(), user);

        recruitService.recruitProject(user.getUserId(), recruitProjectRequest, project.getLeaderId());
    }

    public List<Project> generateProjectRecruits(int index, User user) {
        List<Project> projects = new ArrayList<>();
        IntStream.range(0, index).forEach(i -> {
            Project project = generateProject(i, testUserFactory.generateUser(i + 10), State.RECRUTING);
            generateRecruit(user, project);
            projects.add(project);
        });
        return projects;
    }

    public ProjectDetailRequest generateProjectDetailRequest(int index, State status, ProjectField projectField, Boolean isNeed) {
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        questions.add(new ProjectApplyQuestion("question1"));
        questions.add(new ProjectApplyQuestion("question2"));

        ProjectMemberSet needMember;
        if (isNeed.equals(true))
            needMember = new ProjectMemberSet(0, 0, 0, 0);
        else
            needMember = new ProjectMemberSet(1, 4, 6, 8);

        return ProjectDetailRequest.builder()
                .projectName("project" + index)
                .teamName("project team" + index * 2)
                .endDate(LocalDateTime.now().plusDays(1))
                .introduction("need yes 입니다.")
                .needMember(needMember)
                .state(status)
                .projectField(projectField)
                .questions(questions)
                .applyCanFile(Boolean.TRUE)
                .build();
    }

    public UpdateProfileRequest generateUpdateProfileRequest() {
        List<TechnicalStack> stacks = new ArrayList<TechnicalStack>();
        stacks.add(TechnicalStack.DJANGO);

        return UpdateProfileRequest.builder()
                .area("서울시 구로구")
                .contact("010-9876-5432")
                .introduction("프로필 업데이트 하기")
                .role(ProjectRole.LEADER)
                .stacks(stacks)
                .userName("회원 01")
                .build();
    }

    public ProjectDetailRequest generateProjectUpdateRequest(Project project) {
        return ProjectDetailRequest.builder()
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
    }

    public RecruitProjectRequest generateRecruitRequest(Long projectId, User user) {
        return RecruitProjectRequest.builder()
                .projectId(projectId)
                .introduction("플젝에 영입하고 싶어요")
                .role(user.getRole())
                .build();
    }

    public ProjectApplyRequest generateProjectApplyRequest() {
        List<String> answers = new ArrayList<String>();
        answers.add("1번 응답");
        answers.add("2번 응답");
        answers.add("3번 응답");
        return ProjectApplyRequest.builder()
                .role(ProjectRole.DEVELOPER)
                .introduction("안녕하세요? 저는 그냥 개발자입니다.")
                .answers(answers)
                .build();
    }
}
