package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectPersonSet;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
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
    private String projectDescription;
    private RecruitStatus recruitStatus;
    private ProjectField projectField;
    private Boolean applyCanFile;
    private List<String> questions = new ArrayList<String>();
    private ProjectPersonSet needPerson= new ProjectPersonSet();



    @Builder
    public UpdateDto(String projectName, String teamName, LocalDateTime endDate, String projectDescription, RecruitStatus recruitStatus, ProjectPersonSet needPerson, ProjectField projectField, Boolean applyCanFile, List<ProjectApplyQuestion> questions){
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.projectDescription = projectDescription;
        this.recruitStatus = recruitStatus;
        this.needPerson = needPerson;
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
                project.setProjectDescription(this.projectDescription);
                project.setRecruitStatus(this.recruitStatus);
                project.setProjectField(this.projectField);
                project.setApplyCanFile(this.applyCanFile);
                project.setQuestions(questions);
                project.setNeedPerson(this.needPerson);

        return project;
    }
}
