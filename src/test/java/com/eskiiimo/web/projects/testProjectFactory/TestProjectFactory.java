package com.eskiiimo.web.projects.testProjectFactory;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.repository.projects.repository.ProjectMemberRepository;
import com.eskiiimo.repository.projects.repository.ProjectRepository;
import com.eskiiimo.repository.user.dto.ProfileDto;
import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestProjectFactory {

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMemberRepository projectMemberRepository;

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

    public Project generateProject(int index, User user, State status) {
        Project project = Project.builder()
                .projectName("project" + index)
                .teamName("project team" + index * 2)
                .endDate(LocalDateTime.of(2022, 04, 30, 23, 59))
                .introduction("need yes 입니다.")
                .currentMember(new ProjectMemberSet(2, 1, 1, 2))
                .needMember(new ProjectMemberSet(1, 4, 6, 8))
                .state(status)
                .projectField(ProjectField.APP)
                .leaderId(user.getUserId())
                .build();
        ProjectMember projectMember = ProjectMember.builder()
                .project(project)
                .user(user)
                .hide(Boolean.FALSE)
                .role(ProjectRole.LEADER)
                .introduction("프로젝트 팀장 입니다.")
                .build();
        project.getProjectMembers().add(projectMember);
        this.projectMemberRepository.save(projectMember);
        this.projectRepository.save(project);
        return project;
    }

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
}
