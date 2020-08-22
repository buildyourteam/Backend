package com.eskiiimo.repository.projects.dto;

import com.eskiiimo.repository.projects.model.ProjectApply;
import com.eskiiimo.repository.projects.model.ProjectApplyAnswer;
import com.eskiiimo.repository.projects.model.ProjectApplyQuestion;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectApplyDto {
    private String userName;
    private ProjectApplyState state;
    private List<String> questions;
    private List<String> answers;
    private String introduction;
    private ProjectRole role;

    public ProjectApplyDto(ProjectApply projectApply, List<ProjectApplyQuestion> questions) {
        this.userName = projectApply.getUser().getUserName();
        this.state = projectApply.getState();
        if (questions != null) {
            List<String> questionList = new ArrayList<>();
            for (ProjectApplyQuestion question : questions)
                questionList.add(question.getQuestion());
            this.questions = questionList;
        }
        List<String> answerList = new ArrayList<>();
        for (ProjectApplyAnswer answer : projectApply.getAnswers())
            answerList.add(answer.getAnswer());
        this.answers = answerList;
        this.introduction = projectApply.getIntroduction();
        this.role = projectApply.getRole();
    }
}
