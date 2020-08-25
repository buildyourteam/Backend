package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
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
public class ModifyProjectDto {
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String introduction;
    private State state;
    private ProjectField projectField;
    private Boolean applyCanFile;
    private List<ProjectApplyQuestion> questions;
    private ProjectMemberSet needMember = new ProjectMemberSet();

    public ModifyProjectDto(String projectName, String teamName, LocalDateTime endDate, String introduction, State state, ProjectField projectField, Boolean applyCanFile, List<String> questions, ProjectMemberSet needMember) {
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.introduction = introduction;
        this.state = state;
        this.projectField = projectField;
        this.applyCanFile = applyCanFile;
        this.questions = new ArrayList<ProjectApplyQuestion>();
        for (String question : questions)
            this.questions.add(new ProjectApplyQuestion(question));
        this.needMember = needMember;
    }
}
