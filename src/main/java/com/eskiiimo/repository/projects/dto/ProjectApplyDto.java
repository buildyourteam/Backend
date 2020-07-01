package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.projects.model.ProjectApplyAnswer;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import com.eskiiimo.repository.user.model.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ProjectApplyDto {
    private String userName;
    private ProjectApplyState state;
    private List<String> questions;
    private List<String> answers;
    private String introduction;
    private ProjectRole role;

    @Builder
    public ProjectApplyDto(String userName, ProjectApplyState state, List<ProjectApplyQuestion> questions, List<ProjectApplyAnswer> answers, String introduction, ProjectRole role){
        this.userName = userName;
        this.state = state;
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
        this.introduction = introduction;
        this.role = role;
    }
    public ProjectApply toEntity(User user){
        List<ProjectApplyAnswer> answers = new ArrayList<ProjectApplyAnswer>();
        for(String answer : this.answers)
            answers.add(ProjectApplyAnswer.builder().answer(answer).build());
        ProjectApply projectApply = ProjectApply.builder()
                .answers(answers)
                .introduction(this.introduction)
                .state(ProjectApplyState.UNREAD)
                .user(user)
                .role(role)
                .build();
        return projectApply;
    }
}
