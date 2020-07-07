package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.Project;
import com.eskiiimo.repository.projects.model.ProjectPerson;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.web.projects.controller.resource.ProjectPersonResource;
import com.eskiiimo.web.projects.enumtype.ProjectField;
import com.eskiiimo.web.projects.enumtype.ProjectPersonSet;
import com.eskiiimo.web.projects.enumtype.RecruitStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    private String projectDescription;
    private RecruitStatus recruitStatus;
    private long dday;
    private ProjectField projectField;
    private Boolean applyCanFile;
    private List<String> questions = new ArrayList<String>();
    private ProjectPersonSet currentPerson = new ProjectPersonSet();
    private ProjectPersonSet needPerson= new ProjectPersonSet();
    private List<ProjectPersonResource> personList = new ArrayList<ProjectPersonResource>();


    @Builder
    public ProjectDetailDto(String projectName, String teamName, LocalDateTime endDate, String projectDescription, RecruitStatus recruitStatus, ProjectPersonSet currentPerson, ProjectPersonSet needPerson, List<ProjectPerson> personList, long dDay, ProjectField projectField, Boolean applyCanFile, List<ProjectApplyQuestion> questions){
        this.projectName = projectName;
        this.teamName = teamName;
        this.endDate = endDate;
        this.projectDescription = projectDescription;
        this.recruitStatus = recruitStatus;
        this.currentPerson = currentPerson;
        this.needPerson = needPerson;
        this.dday=dday;
        this.projectField=projectField;
        this.applyCanFile = applyCanFile;
        for(ProjectApplyQuestion question : questions)
            this.questions.add(question.getQuestion());
        if(personList ==null)
            this.personList = null;
        else {
            List<ProjectPersonResource> projectPersonListResource = new ArrayList<ProjectPersonResource>();
            if(!personList.isEmpty())
                for (ProjectPerson projectPerson : personList) {
                    ProjectPersonDto projectPersonDto = ProjectPersonDto.builder()
                            .personName(projectPerson.getPerson().getPersonName())
                            .projectRole(projectPerson.getProjectRole())
                            .stack(projectPerson.getStack())
                            .build();
                    ProjectPersonResource projectPersonResource = new ProjectPersonResource(projectPersonDto, projectPerson.getPerson().getPersonId());
                    projectPersonListResource.add(projectPersonResource);
                }
            this.personList = projectPersonListResource;
        }
    }

    public Project toEntity(Project project){
        List<ProjectApplyQuestion> questions = new ArrayList<ProjectApplyQuestion>();
        for(String question : this.questions)
            questions.add(ProjectApplyQuestion.builder().question(question).build());
                project.setProjectName(this.projectName);
                project.setTeamName(this.teamName);
                project.setEndDate(this.endDate);
                project.setProjectDescription(this.projectDescription);
                if(this.recruitStatus ==null)
                    project.setRecruitStatus(RecruitStatus.RECRUTING);
                else
                    project.setRecruitStatus(this.recruitStatus);
                project.setDday(ChronoUnit.DAYS.between(LocalDateTime.now(), this.endDate));
                project.setProjectField(this.projectField);
                project.setApplyCanFile(this.applyCanFile);
                project.setQuestions(questions);
                if(this.currentPerson==null)
                    project.setCurrentPerson(new ProjectPersonSet(0,0,0,0));
                project.setNeedPerson(this.needPerson);

        return project;
    }
}
