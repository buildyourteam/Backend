package com.eskiiimo.repository.projects.model;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.ProjectApplyState;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "applyId")
@Entity
@Table(name = "T_APPLY")
public class ProjectApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applyId;
    @Enumerated(EnumType.STRING)
    private ProjectApplyState state;
    private String introduction;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "applyId")
    private List<ProjectApplyAnswer> answers;
    @ManyToOne
    @JoinColumn(name = "accountId")
    private User user;

    public ProjectApply(List<String> answers, String introduction, ProjectRole role, User user) {
        this.answers = new ArrayList<ProjectApplyAnswer>();
        for (String answer : answers)
            this.answers.add(new ProjectApplyAnswer(answer));
        this.introduction = introduction;
        this.role = role;
        this.user = user;
        this.state = ProjectApplyState.UNREAD;
    }

    public void updateApply(String introduction, ProjectRole role, List<String> answers) {
        this.introduction = introduction;
        this.role = role;
        for (int i = 0; i < this.answers.size(); i++)
            if (!this.answers.get(i).getAnswer().equals(answers.get(i)))
                this.answers.get(i).updateAnswer(answers.get(i));
    }

    public void markAsRead() {
        if (this.state == ProjectApplyState.UNREAD)
            this.state = ProjectApplyState.READ;
    }

    public void setApplyState(ProjectApplyState state) {
        this.state = state;
    }

}
