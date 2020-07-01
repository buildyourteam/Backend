package com.eskiiimo.repository.projects.model;

import com.eskiiimo.web.projects.enumtype.ProjectRole;
import com.eskiiimo.web.projects.enumtype.TechnicalStack;
import com.eskiiimo.repository.user.model.User;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="projectMemberId")
@Entity
@Table(name = "T_PROJECT_MEMBER")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Long projectMemberId;
    @Enumerated(EnumType.STRING)
    private ProjectRole role;
    @Enumerated(EnumType.STRING)
    private TechnicalStack stack;
    @Lob
    private String selfDescription;

    Boolean hide;

    @ManyToOne
    @JoinColumn(name="accountId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "projectId")
    private Project project;


    public void setProject(Project project){
        this.project = project;
        if(!project.getProjectMembers().contains(this))
            this.project.addMember(this);
    }
}