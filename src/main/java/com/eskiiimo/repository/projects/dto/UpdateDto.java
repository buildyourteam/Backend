package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectMemberSet;
import com.eskiiimo.web.projects.enumtype.State;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateDto {
    private String projectName;
    private String teamName;
    private LocalDateTime endDate;
    private String introduction;
    private State state;
    private ProjectField projectField;
    private Boolean applyCanFile;
    private List<String> questions = new ArrayList<String>();
    private ProjectMemberSet needMember= new ProjectMemberSet();



    @Builder
    public UpdateDto(String projectName, String teamName, LocalDateTime endDate, String introduction, State state, ProjectMemberSet needMember, ProjectField projectField, Boolean applyCanFile, List<ProjectApplyQuestion> questions){
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.introduction = introduction;
        this.state = state;
        this.needMember = needMember;
        this.projectField=projectField;
        this.applyCanFile = applyCanFile;
        for(ProjectApplyQuestion question : questions)
            this.questions.add(question.getQuestion());
    }

    public Project toEntity(Project project){
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        for(String question : this.questions)
            questions.add(ProjectApplyQuestion.builder().question(question).build());
                project.setProjectName(this.projectName);
                project.setTeamName(this.teamName);
                project.setEndDate(this.endDate);
                project.setIntroduction(this.introduction);
                project.setState(this.state);
                project.setProjectField(this.projectField);
                project.setApplyCanFile(this.applyCanFile);
                project.setQuestions(questions);
                project.setNeedMember(this.needMember);

        return project;
    }
}
