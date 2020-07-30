package com.eskiiimo.repository.projects.model;

import com.eskiiimo.repository.user.model.User;
import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "projectMemberId")
@Entity
@Table(name = "T_MEMBER")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberId;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
    @Enumerated(EnumType.STRING)
    private TechnicalStack stack;
    @Lob
    private String introduction;

    Boolean hide;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;


    public void joinProject(Project project) {
        this.project = project;
        if (this.project.getProjectMembers() == null
                || !this.project.getProjectMembers().contains(this))
            this.project.addMember(this);
    }

    public void setVisible(Boolean visible) {
        this.hide = !visible;
    }
}