package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.web.projects.controller.resource.ProjectMemberResource;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.State;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectDetailDto {
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String introduction;
    private State state;
    private ProjectField projectField;
    private Boolean applyCanFile;
    private List<String> questions = new ArrayList<String>();
    private ProjectMemberSet currentMember = new ProjectMemberSet();
    private ProjectMemberSet needMember = new ProjectMemberSet();
    private List<ProjectMemberResource> memberList = new ArrayList<ProjectMemberResource>();

    public ProjectDetailDto(Project project) {
        this.projectName = project.getProjectName();
        this.teamName = project.getTeamName();
        this.endDate = project.getEndDate();
        this.introduction = project.getIntroduction();
        this.state = project.getState();
        this.currentMember = project.getCurrentMember();
        this.needMember = project.getNeedMember();
        this.projectField = project.getProjectField();
        this.applyCanFile = project.getApplyCanFile();
        for (ProjectApplyQuestion question : project.getQuestions())
            this.questions.add(question.getQuestion());
        List<ProjectMemberResource> projectMemberListResource = new ArrayList<ProjectMemberResource>();
        for (ProjectMember projectMember : project.getProjectMembers())
            projectMemberListResource.add(new ProjectMemberResource(new ProjectMemberDto(projectMember), projectMember.getUser().getUserId()));
        this.memberList = projectMemberListResource;

    }
}
