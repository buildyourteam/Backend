package com.eskiiimo.api.projects.apply;

import com.eskiiimo.api.projects.ProjectRole;
import com.eskiiimo.api.projects.apply.entity.ProjectApplyAnswer;
import com.eskiiimo.api.projects.apply.entity.ProjectApply;
import com.eskiiimo.api.projects.apply.entity.ProjectApplyQuestion;
import com.eskiiimo.api.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectApplyDto {
    private String userName;
    private ProjectApplyStatus status;
    private List<String> questions;
    private List<String> answers;
    private String selfDescription;
    private ProjectRole role;

    @Builder
    public ProjectApplyDto(String userName, ProjectApplyStatus status, List<ProjectApplyQuestion> questions, List<ProjectApplyAnswer> answers, String selfDescription, ProjectRole role){
        this.userName = userName;
        this.status = status;
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
        this.role = role;
    }
    public ProjectApply toEntity(User user){
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for(String answer : this.answers)
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());
        ProjectApply projectApply = ProjectApply.builder()
                .answers(answers)
                .selfDescription(this.selfDescription)
                .status(ProjectApplyStatus.UNREAD)
                .user(user)
                .role(role)
                .build();
        return projectApply;
    }
}
