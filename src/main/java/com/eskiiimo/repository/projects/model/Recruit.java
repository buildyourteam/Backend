package com.eskiiimo.repository.projects.model;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.RecruitState;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "recruitId")
@Entity
@Table(name = "T_RECRUIT")
public class Recruit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recruitId;
    @Enumerated(EnumType.STRING)
    private RecruitState state;
    private String introduction;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;

    public void markAsRead() {
        if (this.state == RecruitState.UNREAD)
            this.state = RecruitState.READ;
    }

    public void setRecruitState(RecruitState state) {
        this.state = state;
    }


}
