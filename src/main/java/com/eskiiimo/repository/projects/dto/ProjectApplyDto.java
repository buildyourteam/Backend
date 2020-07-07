package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.projects.model.ProjectApplyAnswer;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.web.projects.enumtype.ProjectApplyStatus;
import com.eskiiimo.repository.person.model.Person;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectApplyDto {
    private String personName;
    private ProjectApplyStatus projectApplyStatus;
    private List<String> questions;
    private List<String> answers;
    private String selfDescription;
    private ProjectRole projectRole;

    @Builder
    public ProjectApplyDto(String personName, ProjectApplyStatus projectApplyStatus, List<ProjectApplyQuestion> questions, List<ProjectApplyAnswer> answers, String selfDescription, ProjectRole projectRole){
        this.personName = personName;
        this.projectApplyStatus = projectApplyStatus;
        if(questions!=null) {
        List<String> questionList = new ArrayList<>();
        for(ProjectApplyQuestion question : questions)
            questionList.add(question.getQuestion());
            this.questions = questionList;
        }
            List<String> answerList = new ArrayList<>();
            for (ProjectApplyAnswer answer : answers)
                answerList.add(answer.getAnswer());
            this.answers = answerList;
        this.selfDescription = selfDescription;
        this.projectRole = projectRole;
    }
    public ProjectApply toEntity(Person person){
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for(String answer : this.answers)
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());
        ProjectApply projectApply = ProjectApply.builder()
                .answers(answers)
                .selfDescription(this.selfDescription)
                .projectApplyStatus(ProjectApplyStatus.UNREAD)
                .person(person)
                .projectRole(projectRole)
                .build();
        return projectApply;
    }
}
