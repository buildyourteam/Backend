package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.repository.projects.model.ProjectMember;
import com.eskiiimo.web.projects.controller.resource.ProjectMemberResource;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.State;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
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


    @Builder
    public ProjectDetailDto(String projectName, String teamName, LocalDateTime endDate, String introduction, State state, ProjectMemberSet currentMember, ProjectMemberSet needMember, List<ProjectMember> memberList, ProjectField projectField, Boolean applyCanFile, List<ProjectApplyQuestion> questions) {
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.introduction = introduction;
        this.state = state;
        this.currentMember = currentMember;
        this.needMember = needMember;
        this.projectField = projectField;
        this.applyCanFile = applyCanFile;
        for (ProjectApplyQuestion question : questions)
            this.questions.add(question.getQuestion());
        if (memberList == null)
            this.memberList = null;
        else {
            List<ProjectMemberResource> projectMemberListResource = new ArrayList<ProjectMemberResource>();
            if (!memberList.isEmpty())
                for (ProjectMember projectMember : memberList) {
                    ProjectMemberDto projectMemberDto = ProjectMemberDto.builder()
                            .userName(projectMember.getUser().getUserName())
                            .role(projectMember.getRole())
                            .stack(projectMember.getStack())
                            .grade(projectMember.getUser().getGrade())
                            .build();
                    ProjectMemberResource projectMemberResource = new ProjectMemberResource(projectMemberDto, projectMember.getUser().getUserId());
                    projectMemberListResource.add(projectMemberResource);
                }
            this.memberList = projectMemberListResource;
        }
    }
}
