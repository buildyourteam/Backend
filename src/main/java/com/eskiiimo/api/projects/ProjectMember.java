package com.eskiiimo.api.projects;

import com.eskiiimo.api.user.User;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of="projectMemberId")
@Entity

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