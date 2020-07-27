package com.eskiiimo.web.configs;

import com.eskiiimo.repository.projects.dto.ProjectDetailDto;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.repository.security.dto.SignUpDto;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.web.projects.service.ProjectDetailService;
import com.eskiiimo.web.security.service.AuthService;
import com.eskiiimo.web.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
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
    }

    private void generateUser(int index) {
        this.authService.signup(
                SignUpDto.builder()
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
                ProfileDto.builder()
                        .area("서울시 어딘가")
                        .contact("010-1234-5678")
                        .grade((long) 0)
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
                ProfileDto.builder()
                        .area("고양시 어딘가")
                        .contact("010-1234-5678")
                        .grade((long) 0)
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
                ProfileDto.builder()
                        .area("안양시 어딘가")
                        .contact("010-1234-5678")
                        .grade((long) 0)
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
        ProjectDetailDto projectDetailDto = ProjectDetailDto.builder()
                .applyCanFile(Boolean.TRUE)
                .endDate(LocalDateTime.now().plusDays(20))
                .needMember(new ProjectMemberSet(2, 2, 2, 2))
                .projectField(projectField)
                .projectName("TestProject" + index)
                .questions(questionList)
                .introduction("테스트입니다.")
                .teamName("프로젝트 " + index)
                .currentMember(null)
                .memberList(null)
                .state(null)
                .build();
        projectDetailDto.setQuestions(questions);
        this.projectDetailService.storeProject(
                projectDetailDto,
                "TestUser" + index
        );
    }
}
