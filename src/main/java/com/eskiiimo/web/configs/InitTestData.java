package com.eskiiimo.web.configs;

import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.web.security.request.SignUpRequest;
import com.eskiiimo.web.projects.enumtype.*;
import com.eskiiimo.web.projects.request.ProjectDetailRequest;
import com.eskiiimo.web.projects.service.ProjectDetailService;
import com.eskiiimo.web.security.service.AuthService;
import com.eskiiimo.web.user.request.UpdateProfileRequest;
import com.eskiiimo.web.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Profile("!test")
@RequiredArgsConstructor
public class InitTestData implements ApplicationListener<ApplicationStartedEvent> {

    private final AuthService authService;
    private final ProjectDetailService projectDetailService;
    private final ProfileService profileService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        for (int i = 1; i <= 15; i++)
            generateUser(i);
        for (int i = 0; i < 5; i++)
            updateProfile(i);
        System.out.println("Init Test User");
        generateProjects();
        System.out.println("Init Test Projects");
        setProject((long) 1, "1", State.ENDED);
        setProject((long) 2, "1", State.ENDED);
        setProject((long) 3, "1", State.RUNNING);
        setProject((long) 4, "1", State.RUNNING);
    }

    private void generateUser(int index) {
        this.authService.signup(
                SignUpRequest.builder()
                        .name("유저" + index)
                        .password("password")
                        .userEmail("TestUser" + index + "@egluu.com")
                        .userId("TestUser" + index)
                        .build());
    }

    private void updateProfile(int index) {
        List<TechnicalStack> stacks1 = new ArrayList<TechnicalStack>();
        stacks1.add(TechnicalStack.SPRINGBOOT);
        this.profileService.updateProfile(
                "TestUser" + (index * 3 + 1),
                "TestUser" + (index * 3 + 1),
                UpdateProfileRequest.builder()
                        .area("서울시")
                        .contact("010-1234-5678")
                        .introduction("테스트 유저" + (index * 3 + 1) + " 입니다.")
                        .role(ProjectRole.DEVELOPER)
                        .stacks(stacks1)
                        .userName("유저" + (index * 3 + 1))
                        .build()
        );

        List<TechnicalStack> stacks2 = new ArrayList<TechnicalStack>();
        stacks1.add(TechnicalStack.DJANGO);
        this.profileService.updateProfile(
                "TestUser" + (index * 3 + 2),
                "TestUser" + (index * 3 + 2),
                UpdateProfileRequest.builder()
                        .area("대구시")
                        .contact("010-1234-5678")
                        .introduction("테스트 유저" + (index * 3 + 2) + " 입니다.")
                        .role(ProjectRole.DEVELOPER)
                        .stacks(stacks2)
                        .userName("유저" + (index * 3 + 2))
                        .build()
        );

        List<TechnicalStack> stacks3 = new ArrayList<TechnicalStack>();
        stacks1.add(TechnicalStack.SPRINGBOOT);
        stacks1.add(TechnicalStack.GO);
        this.profileService.updateProfile(
                "TestUser" + (index * 3 + 3),
                "TestUser" + (index * 3 + 3),
                UpdateProfileRequest.builder()
                        .area("부산시")
                        .contact("010-1234-5678")
                        .introduction("테스트 유저" + (index * 3 + 3) + " 입니다.")
                        .role(ProjectRole.DEVELOPER)
                        .stacks(stacks3)
                        .userName("유저" + (index * 3 + 3))
                        .build()
        );
    }

    private void generateProjects() {
        for (int i = 1; i < 15; i += 5) {
            generateProject(i, ProjectField.WEB);
            generateProject(i + 1, ProjectField.APP);
            generateProject(i + 2, ProjectField.AI);
            generateProject(i + 3, ProjectField.BLOCKCHAIN);
            generateProject(i + 4, ProjectField.HW);
        }
    }

    private void generateProject(int index, ProjectField projectField) {
        List<String> questions = new ArrayList<String>();
        List<ProjectApplyQuestion> questionList = new ArrayList<ProjectApplyQuestion>();
        questions.add("왜 하고싶어요?");
        ProjectDetailRequest projectDetailRequest = ProjectDetailRequest.builder()
                .applyCanFile(Boolean.TRUE)
                .endDate(LocalDateTime.now().plusDays(20))
                .needMember(new ProjectMemberSet(2, 2, 2, 2))
                .projectField(projectField)
                .projectName("TestProject" + index)
                .questions(questionList)
                .introduction("테스트입니다.")
                .teamName("프로젝트 " + index)
                .state(State.RECRUTING)
                .build();
        projectDetailRequest.setQuestions(questions);
        this.projectDetailService.storeProject(
                projectDetailRequest,
                "TestUser" + ((index - 1) / 5 + 1)
        );
    }

    private void setProject(Long projectId, String visitorId, State state) {
        List<String> questions = new ArrayList<String>();
        List<ProjectApplyQuestion> questionList = new ArrayList<ProjectApplyQuestion>();
        questions.add("왜 하고싶어요?");
        ProjectDetailRequest projectDetailRequest = ProjectDetailRequest.builder()
                .projectName("TestProject" + projectId)
                .teamName("프로젝트 " + projectId)
                .endDate(LocalDateTime.now().plusDays(20))
                .introduction("테스트입니다.")
                .state(state)
                .projectField(ProjectField.WEB)
                .applyCanFile(Boolean.TRUE)
                .questions(questionList)
                .needMember(new ProjectMemberSet(2, 2, 2, 2))
                .build();
        projectDetailRequest.setQuestions(questions);
        this.projectDetailService.updateProject(
                projectId,
                projectDetailRequest,
                "TestUser" + visitorId
        );
    }
}
