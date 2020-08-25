package com.eskiiimo.web.projects.request;

import com.eskiiimo.repository.projects.dto.ModifyProjectDto;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
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
public class ProjectDetailRequest {
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String introduction;
    private State state;
    private ProjectField projectField;
    private Boolean applyCanFile;
    private List<String> questions = new ArrayList<String>();
    private ProjectMemberSet needMember = new ProjectMemberSet();


    @Builder
    public ProjectDetailRequest(String projectName, String teamName, LocalDateTime endDate, String introduction, State state, ProjectMemberSet needMember, ProjectField projectField, Boolean applyCanFile, List<ProjectApplyQuestion> questions) {
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.introduction = introduction;
        this.state = state;
        this.needMember = needMember;
        this.projectField = projectField;
        this.applyCanFile = applyCanFile;
        for (ProjectApplyQuestion question : questions)
            this.questions.add(question.getQuestion());
    }

    public ModifyProjectDto toModifyProjectDto() {
        return new ModifyProjectDto(
                this.projectName,
                this.teamName,
                this.endDate,
                this.introduction,
                this.state,
                this.projectField,
                this.applyCanFile,
                this.questions,
                this.needMember
        );
    }
}
